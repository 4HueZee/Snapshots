package com.example.battlebarge.engine

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.battlebarge.agnostic.data.local.EdenDatabase
import com.battlebarge.agnostic.data.parser.SingularityParser
import com.battlebarge.agnostic.data.remote.DiscoveryService
import com.battlebarge.agnostic.data.remote.FilterProvider
import com.battlebarge.agnostic.data.remote.GithubApiService
import com.battlebarge.agnostic.data.remote.RuleDownloader
import com.battlebarge.agnostic.domain.logic.RuleEngine
import com.battlebarge.agnostic.domain.repository.ArgonautRepository
import com.battlebarge.agnostic.domain.repository.SingularityRepository
import com.battlebarge.agnostic.domain.repository.SnapshotSource
import com.battlebarge.agnostic.domain.repository.StreamingSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * The Nervous System of the app. 
 * Provides a single, managed instance of the Agnostic Engine.
 */
object EngineCore {
    @Volatile
    private var singularityRepository: SingularityRepository? = null
    @Volatile
    private var argonautRepository: ArgonautRepository? = null
    @Volatile
    private var ruleEngine: RuleEngine? = null

    fun initialize(context: Context) {
        if (singularityRepository != null) return

        synchronized(this) {
            if (singularityRepository != null) return

            // 1. Setup Database (Eden)
            val database = Room.databaseBuilder(
                context.applicationContext,
                EdenDatabase::class.java,
                "eden_cache.db",
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    try {
                        db.query("PRAGMA journal_mode = WAL").close()
                        db.query("PRAGMA mmap_size = 268435456").close()
                        db.query("PRAGMA cache_size = -2000").close()
                        db.query("PRAGMA synchronous = NORMAL").close()
                    } catch (e: Exception) {
                        android.util.Log.e("EngineCore", "Failed to tune database", e)
                    }
                }
            })
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

            // 2. Setup Networking (The Telescope with 30s Timeouts)
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            val apiService = retrofit.create(GithubApiService::class.java)
            val downloader = RuleDownloader(okHttpClient)

            // 3. Setup Sources (Modular Tracks)
            val parser = SingularityParser()
            val filterProvider = FilterProvider(okHttpClient)
            val discoveryService = DiscoveryService(apiService, downloader, database, filterProvider)
            
            val streamingSource = StreamingSource(
                apiService = apiService,
                discoveryService = discoveryService,
                downloader = downloader,
                parser = parser,
                database = database
            )

            val snapshotSource = SnapshotSource(
                context = context.applicationContext,
                discoveryService = discoveryService,
                downloader = downloader,
                database = database
            )

            // 4. Setup Orchestrator
            singularityRepository = SingularityRepository(
                streamingSource = streamingSource,
                apiService = apiService,
                prebuiltSource = snapshotSource,
                database = database
            )

            argonautRepository = ArgonautRepository(database)
            ruleEngine = RuleEngine(singularityRepository!!)
        }
    }

    fun provideRepository(context: Context? = null): SingularityRepository {
        if (singularityRepository == null && context != null) {
            initialize(context.applicationContext)
        }
        return singularityRepository ?: throw IllegalStateException("EngineCore must be initialized before use")
    }

    fun provideArgonautRepository(context: Context? = null): ArgonautRepository {
        if (argonautRepository == null && context != null) {
            initialize(context.applicationContext)
        }
        return argonautRepository ?: throw IllegalStateException("EngineCore must be initialized before use")
    }

    fun provideRuleEngine(context: Context? = null): RuleEngine {
        if (ruleEngine == null && context != null) {
            initialize(context.applicationContext)
        }
        return ruleEngine ?: throw IllegalStateException("EngineCore must be initialized before use")
    }
}
