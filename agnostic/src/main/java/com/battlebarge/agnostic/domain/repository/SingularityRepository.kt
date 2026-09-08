package com.battlebarge.agnostic.domain.repository

import android.util.Log
import com.battlebarge.agnostic.data.local.EdenDatabase
import com.battlebarge.agnostic.data.local.DownloadedFaction
import com.battlebarge.agnostic.data.local.DownloadedGameSystem
import com.battlebarge.agnostic.data.local.GameSystemMetadataEntity
import com.battlebarge.agnostic.data.local.toDomain
import com.battlebarge.agnostic.data.remote.DiscoveredGame
import com.battlebarge.agnostic.data.remote.GithubApiService
import com.battlebarge.agnostic.domain.model.Singularity
import com.battlebarge.agnostic.domain.model.SingularityState
import com.battlebarge.agnostic.domain.model.UnitDatasheetModule
import com.battlebarge.agnostic.domain.model.WeaponProfile
import com.battlebarge.agnostic.domain.model.AbilityRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * The "Headless Engine" - Orchestrates multiple RuleSources (Streaming/Snapshots).
 * Manages rule resolution, database integrity, and high-level sync operations.
 */
class SingularityRepository(
    private val streamingSource: RuleSource,
    private val apiService: GithubApiService, // Re-added for RepoPreview
    private val prebuiltSource: RuleSource? = null,
    private val database: EdenDatabase,
    private val engineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) {
    private val dao = database.singularityDao()

    private val _engineState = MutableStateFlow<SingularityState>(SingularityState.Idle)
    val engineState: StateFlow<SingularityState> = _engineState.asStateFlow()

    private var activeSource: RuleSource = streamingSource // Default to local streaming reliability

    /**
     * Toggles the data ingestion method.
     */
    fun setIngestionMode(usePrebuilt: Boolean) {
        activeSource = if (usePrebuilt && prebuiltSource != null) prebuiltSource else streamingSource
    }

    /**
     * Flow of faction IDs currently synced to Eden.
     */
    val downloadedFactionIds: Flow<List<String>> = dao.getDownloadedFactionIds()

    /**
     * Flow of full faction metadata for all downloaded rules.
     */
    val downloadedFactions: Flow<List<DownloadedFaction>> = dao.getDownloadedFactions()

    /**
     * Flow of army catalogues (.cat entries) for a specific installed game system.
     */
    suspend fun getDownloadedCataloguesDirect(gsId: String): List<DownloadedFaction> =
        dao.getDownloadedCataloguesDirect(gsId)

    fun getDownloadedCataloguesForSystem(gsId: String): Flow<List<DownloadedFaction>> {
        return dao.getDownloadedCataloguesForSystem(gsId)
    }

    /**
     * Flow of distinct Game Systems that have been downloaded.
     */
    val downloadedGameSystems: Flow<List<DownloadedGameSystem>> = dao.getDownloadedGameSystems()

    /**
     * Identifies available game systems in the library using the active source.
     */
    suspend fun discoverLibrary(): List<DiscoveredGame> = activeSource.getLibrary()

    /**
     * Triggers a full system sync.
     */
    suspend fun syncArsenal(
        owner: String,
        repo: String,
        branch: String = "main"
    ): Result<Unit> = withContext(Dispatchers.IO) {
        _engineState.value = SingularityState.Loading("Syncing Arsenal...", 0, gsId = repo)
        var result = activeSource.transcribeSystem(owner, repo, branch)
        
        // Automatic Fallback to Streaming if Snapshot fails
        if (result.isFailure && activeSource is SnapshotSource) {
            Log.w("SingularityRepository", "Snapshot sync failed for $repo, falling back to XML Streaming.")
            _engineState.value = SingularityState.Loading("Fallback: Streaming XML...", 0, gsId = repo)
            result = streamingSource.transcribeSystem(owner, repo, branch)
        }

        if (result.isSuccess) {
            dao.updateLastUpdated(repo)
            _engineState.value = SingularityState.Success(null, repo)
        } else {
            _engineState.value = SingularityState.Error(result.exceptionOrNull()?.message ?: "Sync Failed")
        }
        result
    }

    /**
     * Triggers a core-only transcription.
     */
    suspend fun transcribeCore(
        owner: String,
        repo: String,
        displayName: String? = null,
        branch: String? = null
    ): Result<Unit> = withContext(Dispatchers.IO) {
        // Ensure metadata exists under both repo and canonical aliases
        if (displayName != null) {
            val now = System.currentTimeMillis()
            dao.insertMetadata(
                com.battlebarge.agnostic.data.local.GameSystemMetadataEntity(
                    repoName = repo,
                    displayName = displayName,
                    owner = owner,
                    lastUpdated = now
                )
            )
        }

        var result: Result<Unit>? = null
        if (activeSource is SnapshotSource) {
            result = activeSource.transcribeSystem(owner, repo, branch)
            if (result.isFailure) {
                Log.w("SingularityRepository", "Snapshot core transcribe failed for $repo, falling back to XML Streaming.")
                result = null
            }
        }

        if (result == null || result.isFailure) {
            result = streamingSource.transcribeSystem(owner, repo, branch)
        }

        if (result.isSuccess) {
            dao.updateLastUpdated(repo)
        }

        result
    }

    /**
     * Resolves a relational link to its true source.
     * Follows targetId references across factions and files with cycle detection.
     */
    suspend fun resolve(singularity: Singularity, visited: Set<String> = emptySet()): Singularity {
        val targetId = singularity.targetId ?: return singularity
        
        // Normalize for safety
        val normalizedTarget = targetId.trim()
        
        // Prevent infinite recursion in circular data links
        if (visited.contains(normalizedTarget)) return singularity
        
        // Find the target in the same game system
        val targetEntity = dao.getSingularityByTargetId(singularity.gamesystemId, normalizedTarget)
        val target = targetEntity?.toDomain()
        
        return if (target != null) {
            // Recursive resolution
            val resolvedTarget = resolve(target, visited + singularity.id)
            resolvedTarget.copy(
                // Overlay the link's name if the link has a custom display name
                name = if (singularity.name != singularity.xmlTag) singularity.name else resolvedTarget.name
            )
        } else {
            singularity
        }
    }

    /**
     * Removes all rule data for an entire game system.
     */
    suspend fun removeGameSystem(gsId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            dao.clearGameSystemData(gsId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Cannot remove engine: Data in use."))
        }
    }

    /**
     * Erases the entire rules cache from Eden.
     */
    suspend fun clearCache(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            dao.clearAllRules()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to clear cache: Ensure your rosters are empty first."))
        }
    }

    fun getChildrenOf(gsId: String, fId: String, parentId: String): Flow<List<Singularity>> {
        return dao.getChildrenOf(gsId, fId, parentId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getCharacteristicsForSingularity(gsId: String, fId: String, singularityId: String) =
        dao.getCharacteristicsForSingularity(gsId, fId, singularityId)

    fun getUnitProfilesForUnitAndModels(gsId: String, fId: String, unitId: String) =
        dao.getUnitProfilesForUnitAndModels(gsId, fId, unitId)

    fun getWargearGroupsForModel(gsId: String, fId: String, modelId: String) =
        dao.getWargearGroupsForModel(gsId, fId, modelId).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getCostsForSingularity(gsId: String, fId: String, singularityId: String) =
        dao.getCostsForSingularity(gsId, fId, singularityId)

    fun getCategoryLinksForSingularity(gsId: String, fId: String, singularityId: String) =
        dao.getCategoryLinksForSingularity(gsId, fId, singularityId)

    fun getKeywordsForUnit(gsId: String, fId: String, unitId: String) =
        dao.getKeywordsForUnit(gsId, fId, unitId)

    fun getRootNodes(gsId: String, fId: String): Flow<List<Singularity>> {
        return dao.getRootNodes(gsId, fId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getByTag(gsId: String, fId: String, tag: String): Flow<List<Singularity>> {
        return if (fId == "ALL" || fId == "CORE" || fId.isBlank()) {
            dao.getSingularitiesByTagForSystem(gsId, tag).map { entities ->
                entities.map { it.toDomain() }
            }
        } else {
            dao.getSingularitiesByTag(gsId, fId, tag).map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }

    suspend fun resolveUnitCategory(singularity: Singularity): String {
        if (!singularity.category.isNullOrBlank()) {
            return singularity.category
        }
        val tags = dao.getTagsForSingularity(singularity.gamesystemId, singularity.factionId, singularity.id)
        val allDescriptors = (tags + singularity.xmlTag + singularity.name + (singularity.linkType ?: "")).map { it.uppercase() }

        return when {
            allDescriptors.any { 
                it.contains("CHARACTER") || it.contains("HQ") || it.contains("HERO") || 
                it.contains("WARLORD") || it.contains("CAPTAIN") || it.contains("LORD") || 
                it.contains("COMMAND") || it.contains("COMMISSAR") || it.contains("PSYKER") || 
                it.contains("PRIEST") || it.contains("OFFICER") || it.contains("GENERAL") || 
                it.contains("LEADER") || it.contains("CHIEF") || it.contains("CHAMPION") || 
                it.contains("WARBOSS") || it.contains("OVERLORD") || it.contains("MARSHAL") ||
                it.contains("ARCHON") || it.contains("FARSEER") || it.contains("AUTARCH")
            } -> "CHARACTERS"
            
            allDescriptors.any { 
                it.contains("BATTLELINE") || it.contains("TROOPS") || it.contains("CORE") || 
                it.contains("SQUAD") || it.contains("INFANTRY") || it.contains("BOYZ") || 
                it.contains("WARRIORS") || it.contains("GUARDIANS") || it.contains("GAUNTS") || 
                it.contains("GUARDSMEN") || it.contains("VETERANS")
            } -> "BATTLELINE"
            
            allDescriptors.any { 
                it.contains("TRANSPORT") || it.contains("RHINO") || it.contains("RAIDER") || 
                it.contains("TRUKK") || it.contains("DEVILFISH") || it.contains("IMPULSOR") || 
                it.contains("CHIMERA") || it.contains("DROPSHIP")
            } -> "DEDICATED TRANSPORTS"
            
            else -> "OTHER UNITS"
        }
    }

    fun getNativeCategoriesForFaction(gsId: String, fId: String): Flow<List<String>> {
        return dao.getRootUnitsForFaction(gsId, fId).map { units ->
            val categories = units.mapNotNull { it.category }.distinct().filter { it.isNotBlank() }
            if (categories.isNotEmpty()) categories else listOf("CHARACTERS", "BATTLELINE", "DEDICATED TRANSPORTS", "OTHER UNITS")
        }.flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUnitDatasheetModule(gsId: String, fId: String, unitId: String): Flow<UnitDatasheetModule?> {
        return combine(
            dao.getSingularityByIdFlow(gsId, unitId),
            dao.getCharacteristicsForSingularity(gsId, fId, unitId),
            dao.getChildrenOf(gsId, fId, unitId)
        ) { unitEntity, statsEntities, childrenEntities ->
            val unit = unitEntity?.toDomain() ?: return@combine null

            val physStats = statsEntities
                .filter { it.profileType.contains("Unit", ignoreCase = true) || it.profileType.contains("Model", ignoreCase = true) }
                .associate { it.statName to it.statValue }

            val ranged = mutableListOf<WeaponProfile>()
            val melee = mutableListOf<WeaponProfile>()
            val abilities = mutableListOf<AbilityRule>()

            childrenEntities.forEach { childEntity ->
                val childDomain = childEntity.toDomain()
                val resolved = resolve(childDomain)
                val childStats = dao.getCharacteristicsList(gsId, fId, resolved.id)

                val isRanged = childStats.any { it.profileType.contains("Ranged", ignoreCase = true) } || resolved.name.contains("Rifle", ignoreCase = true) || resolved.name.contains("Cannon", ignoreCase = true)
                val isMelee = childStats.any { it.profileType.contains("Melee", ignoreCase = true) } || resolved.name.contains("Sword", ignoreCase = true) || resolved.name.contains("Weapon", ignoreCase = true)

                if (isRanged || isMelee) {
                    val statMap = childStats.associate { it.statName to it.statValue }
                    val profile = WeaponProfile(
                        id = resolved.id,
                        name = resolved.name,
                        range = statMap["Range"] ?: statMap["R"] ?: "-",
                        attacks = statMap["A"] ?: statMap["Attacks"] ?: "1",
                        skill = statMap["BS"] ?: statMap["WS"] ?: "3+",
                        strength = statMap["S"] ?: statMap["Str"] ?: "4",
                        ap = statMap["AP"] ?: "0",
                        damage = statMap["D"] ?: statMap["Damage"] ?: "1",
                        keywords = dao.getTagsForSingularity(gsId, fId, resolved.id),
                        targetId = resolved.targetId
                    )
                    if (isRanged) ranged.add(profile) else melee.add(profile)
                } else if (resolved.xmlTag == "rule" || resolved.xmlTag == "infoLink") {
                    abilities.add(AbilityRule(resolved.id, resolved.name, resolved.value, resolved.targetId))
                }
            }

            UnitDatasheetModule(
                unit = unit,
                physicalStats = physStats,
                rangedWeapons = ranged,
                meleeWeapons = melee,
                abilities = abilities
            )
        }.flowOn(Dispatchers.IO)
    }

    fun getTopLevelEntriesForFaction(gsId: String, fId: String): Flow<List<Singularity>> {
        return dao.getTopLevelEntriesForFaction(gsId, fId).map { entities ->
            entities.map { it.toDomain() }
        }.flowOn(Dispatchers.IO)
    }

    fun getRootUnitsForCategory(gsId: String, fId: String, category: String): Flow<List<Singularity>> {
        val queryFlow = if (fId.isBlank() || fId == "ALL") {
            dao.getRootUnitsForSystem(gsId)
        } else {
            dao.getRootUnitsForFaction(gsId, fId)
        }

        return queryFlow.map { entities ->
            val allRootUnits = entities.map { it.toDomain() }
            val categorizedUnits = allRootUnits.filter { singularity ->
                val resolved = resolve(singularity)
                val unitCategory = resolveUnitCategory(resolved)
                unitCategory.equals(category, ignoreCase = true)
            }

            // Fallback: If no units matched this specific category keyword filter, 
            // return all root units so the user is NEVER blocked by an empty list
            if (categorizedUnits.isNotEmpty()) {
                categorizedUnits
            } else {
                allRootUnits
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Lists all rule files (.gst, .cat) within a specific repository.
     */
    suspend fun getManifest(owner: String, repo: String, branch: String? = null) = 
        activeSource.getManifest(owner, repo, branch)

    /**
     * Triggers a specific faction transcription.
     */
    suspend fun transcribeFaction(
        owner: String,
        repo: String,
        path: String,
        branch: String = "main",
        remoteSha: String,
        gsId: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val manifest = activeSource.getManifest(owner, repo, branch)
        val source = manifest.find { it.fileName == path }
            ?: return@withContext Result.failure(Exception("Source file not found in manifest"))
        
        activeSource.transcribe(source, gsId)
    }

    suspend fun getSingularityById(gsId: String, fId: String, id: String): Singularity? {
        return dao.getSingularityById(gsId, fId, id)?.toDomain()
    }

    suspend fun getChildren(gsId: String, fId: String, parentId: String): List<Singularity> {
        return dao.getChildrenList(gsId, fId, parentId).map { it.toDomain() }
    }

    suspend fun getLatestShaForFaction(gsId: String, fId: String): String? {
        return dao.getSyncMetadataByFaction(gsId, fId)?.sha
    }

    /**
     * Fetches a preview of rule files from a GitHub repository without syncing.
     */
    suspend fun fetchRepoPreview(owner: String, repo: String, branch: String = "main"): List<RepoFilePreview> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTree(owner, repo, branch, recursive = 0)
            if (response.isSuccessful) {
                response.body()?.tree?.filter { it.path.endsWith(".gst") || it.path.endsWith(".cat") }?.map {
                    RepoFilePreview(
                        name = it.path,
                        sha = it.sha,
                        size = it.size ?: 0
                    )
                } ?: emptyList()
            } else emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }
}

data class RepoFilePreview(
    val name: String,
    val sha: String,
    val size: Long
)
