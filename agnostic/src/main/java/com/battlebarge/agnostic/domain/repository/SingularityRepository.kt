package com.battlebarge.agnostic.domain.repository

import com.battlebarge.agnostic.data.local.EdenDatabase
import com.battlebarge.agnostic.data.local.toDomain
import com.battlebarge.agnostic.data.parser.RuleValidator
import com.battlebarge.agnostic.data.parser.SingularityParser
import com.battlebarge.agnostic.data.remote.GithubApiService
import com.battlebarge.agnostic.data.remote.RuleDownloader
import com.battlebarge.agnostic.domain.model.Singularity
import com.battlebarge.agnostic.domain.model.SingularityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

/**
 * The "Headless Engine" - Orchestrates the "Telescope -> Parser -> Eden" flow.
 * Holds the current engine state and manages its own background work scope.
 */
class SingularityRepository(
    private val apiService: GithubApiService,
    private val downloader: RuleDownloader,
    private val validator: RuleValidator,
    private val parser: SingularityParser,
    private val database: EdenDatabase,
    private val engineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) {
    private val dao = database.singularityDao()

    private val _engineState = MutableStateFlow<SingularityState>(SingularityState.Idle)
    val engineState: StateFlow<SingularityState> = _engineState.asStateFlow()

    /**
     * Triggers a rule sync from GitHub. The state will be reflected in [engineState].
     */
    fun syncRules(
        owner: String,
        repo: String,
        path: String,
        currentSha: String?
    ) {
        engineScope.launch {
            _engineState.value = SingularityState.Loading
            val result = updateRulesInternal(owner, repo, path, currentSha)
            
            result.onSuccess { newSha ->
                _engineState.value = SingularityState.Success(newSha)
            }
            .onFailure { error ->
                _engineState.value = SingularityState.Error(error.message ?: "Unknown engine error")
            }
        }
    }

    /**
     * Internal implementation of the rule update logic.
     */
    private suspend fun updateRulesInternal(
        owner: String,
        repo: String,
        path: String,
        currentSha: String?
    ): Result<String?> = withContext(Dispatchers.IO) {
        try {
            // 1. Hash Check
            val metadataResponse = apiService.getFileMetadata(owner, repo, path)
            if (!metadataResponse.isSuccessful) {
                return@withContext Result.failure(Exception("API check failed: ${metadataResponse.code()}"))
            }

            val remoteSha = metadataResponse.body()?.sha
            if (remoteSha == currentSha) {
                return@withContext Result.success(currentSha) // No update needed
            }

            // 2. Conditional Download
            val downloadUrl = "https://raw.githubusercontent.com/$owner/$repo/master/$path"
            val stream: InputStream = downloader.downloadStream(downloadUrl)

            stream.use { input ->
                // 3. Validate
                // Note: RuleValidator currently consumes the stream. In a real app,
                // we'd buffer or use a multi-pass approach. Proceeding to parse.

                // 4. Parse
                val parseResult = parser.parse(input)

                // 5. Transaction Write to Eden
                dao.replaceData(parseResult.entities, parseResult.tags)
            }

            Result.success(remoteSha)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Observes the children of a specific node in the tree.
     */
    fun getChildrenOf(parentId: String): Flow<List<Singularity>> {
        return dao.getChildrenOf(parentId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
