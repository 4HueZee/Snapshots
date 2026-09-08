package com.battlebarge.agnostic.domain.repository

import android.util.Log
import androidx.room.withTransaction
import com.battlebarge.agnostic.data.local.EdenDatabase
import com.battlebarge.agnostic.data.local.FactionEntity
import com.battlebarge.agnostic.data.local.GameSystemEntity
import com.battlebarge.agnostic.data.local.SyncMetadataEntity
import com.battlebarge.agnostic.data.local.UnitDatasheetEntity
import com.battlebarge.agnostic.data.parser.SingularityParser
import com.battlebarge.agnostic.data.remote.DiscoveredGame
import com.battlebarge.agnostic.data.remote.DiscoveredSource
import com.battlebarge.agnostic.data.remote.DiscoveryService
import com.battlebarge.agnostic.data.remote.GithubApiService
import com.battlebarge.agnostic.data.remote.RuleDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream

/**
 * Method 2: Raw XML Streaming.
 * On-demand local parsing of community rules directly from GitHub using Room withTransaction.
 */
class StreamingSource(
    private val apiService: GithubApiService,
    private val discoveryService: DiscoveryService,
    private val downloader: RuleDownloader,
    private val parser: SingularityParser,
    private val database: EdenDatabase
) : RuleSource {
    private val dao = database.singularityDao()

    override suspend fun getLibrary(): List<DiscoveredGame> = discoveryService.discoverLibrary()

    override suspend fun getManifest(owner: String, repo: String, branch: String?): List<DiscoveredSource> {
        return discoveryService.discoverSources(owner, repo, branch = branch)
    }

    override suspend fun transcribe(source: DiscoveredSource, gsId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            database.withTransaction {
                downloader.downloadStream(source.downloadUrl).use { stream ->
                    val buffered = BufferedInputStream(stream)
                    
                    parser.parseInBatches(
                        inputStream = buffered,
                        gamesystemId = gsId,
                        gamesystemName = source.name,
                        factionId = source.id,
                        factionName = source.name,
                        batchSize = 250
                    ) { entities, tags ->
                        dao.upsertFactionData(gsId, source.id, entities, tags)
                    }

                    dao.insertSyncMetadata(
                        SyncMetadataEntity(
                            filePath = source.fileName,
                            gameSystemId = gsId,
                            sha = source.sha,
                            factionId = source.id
                        )
                    )
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun transcribeSystem(owner: String, repo: String, branch: String?): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val targetBranch = branch ?: "main"
            val manifest = getManifest(owner, repo, targetBranch)

            if (manifest.isEmpty()) {
                return@withContext Result.failure(Exception("Manifest for $repo returned no files"))
            }

            database.withTransaction {
                val engineDao = database.engineDao()
                engineDao.insertGameSystem(
                    GameSystemEntity(
                        id = repo,
                        name = repo,
                        owner = owner,
                        isInstalled = true
                    )
                )

                manifest.forEach { source ->
                    val rawUrl = if (source.downloadUrl.isNotBlank()) {
                        source.downloadUrl
                    } else {
                        "https://raw.githubusercontent.com/$owner/$repo/$targetBranch/${source.fileName.replace(" ", "%20")}"
                    }
                    val factionName = source.fileName.removeSuffix(".cat").removeSuffix(".gst")
                    
                    try {
                        downloader.downloadStream(rawUrl).use { stream ->
                            val buffered = BufferedInputStream(stream)
                            parser.parseInBatches(buffered, repo, repo, factionName, factionName, 250) { entities, tags ->
                                dao.upsertFactionData(repo, factionName, entities, tags)

                                val datasheets = entities.filter { it.xmlTag == "selectionEntry" || it.xmlTag == "entryLink" }.map { e ->
                                    UnitDatasheetEntity(
                                        id = e.id,
                                        gamesystemId = repo,
                                        factionId = factionName,
                                        name = e.name,
                                        category = e.category ?: "UNITS",
                                        basePoints = e.value?.toIntOrNull() ?: 85
                                    )
                                }
                                if (datasheets.isNotEmpty()) engineDao.insertDatasheets(datasheets)
                            }
                            engineDao.insertFactions(
                                listOf(
                                    FactionEntity(
                                        id = factionName,
                                        name = factionName,
                                        gamesystemId = repo,
                                        gamesystemName = repo
                                    )
                                )
                            )
                        }
                    } catch (catEx: Exception) {
                        Log.w("StreamingSource", "Could not stream file ${source.fileName}: ${catEx.message}")
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("StreamingSource", "Failed streaming system $repo", e)
            Result.failure(e)
        }
    }
}
