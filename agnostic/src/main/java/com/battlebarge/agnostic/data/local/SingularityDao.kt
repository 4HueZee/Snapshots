package com.battlebarge.agnostic.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SingularityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArgonaut(argonaut: ArgonautEntity)

    @Query("DELETE FROM singularities WHERE gamesystem_id = :gamesystemId AND faction_id = :factionId")
    suspend fun clearFactionData(gamesystemId: String, factionId: String)

    @Transaction
    suspend fun clearAllRules() {
        clearAllSingularities()
        clearAllTags()
        clearAllCategoryLinks()
        clearAllCosts()
        clearAllCharacteristics()
        clearAllConstraints()
        clearAllSyncMetadata()
        clearAllGameSystemMetadata()
    }

    @Transaction
    suspend fun clearUserDatabase() {
        clearAllArgonauts()
        clearAllRosters()
    }

    @Query("DELETE FROM argonauts")
    suspend fun clearAllArgonauts()

    @Query("DELETE FROM rosters")
    suspend fun clearAllRosters()

    @Query("DELETE FROM singularities")
    suspend fun clearAllSingularities()

    @Query("DELETE FROM singularity_tags")
    suspend fun clearAllTags()

    @Query("DELETE FROM category_links")
    suspend fun clearAllCategoryLinks()

    @Query("DELETE FROM costs")
    suspend fun clearAllCosts()

    @Query("DELETE FROM characteristics")
    suspend fun clearAllCharacteristics()

    @Query("DELETE FROM constraints")
    suspend fun clearAllConstraints()

    @Query("DELETE FROM sync_metadata")
    suspend fun clearAllSyncMetadata()

    @Query("DELETE FROM game_system_metadata")
    suspend fun clearAllGameSystemMetadata()

    @Query("SELECT * FROM singularities WHERE gamesystem_id = :gsId AND faction_id = :fId AND id = :id")
    suspend fun getSingularityById(gsId: String, fId: String, id: String): SingularityEntity?

    @Query("SELECT * FROM singularities WHERE gamesystem_id = :gsId AND id = :id LIMIT 1")
    suspend fun getSingularityByTargetId(gsId: String, id: String): SingularityEntity?

    @Query("SELECT * FROM singularities WHERE gamesystem_id = :gsId AND faction_id = :fId AND parent_id = :parentId")
    fun getChildrenOf(gsId: String, fId: String, parentId: String): Flow<List<SingularityEntity>>

    @Query("SELECT * FROM singularities WHERE gamesystem_id = :gsId AND faction_id = :fId AND parent_id = :parentId")
    suspend fun getChildrenList(gsId: String, fId: String, parentId: String): List<SingularityEntity>

    @Query("SELECT * FROM singularities WHERE gamesystem_id = :gsId AND faction_id = :fId AND parent_id IS NULL")
    fun getRootNodes(gsId: String, fId: String): Flow<List<SingularityEntity>>

    @Query("SELECT tag FROM singularity_tags WHERE gamesystem_id = :gsId AND faction_id = :fId AND singularity_id = :sId")
    suspend fun getTagsForSingularity(gsId: String, fId: String, sId: String): List<String>

    @Query("SELECT * FROM argonauts WHERE id = :id")
    suspend fun getArgonautById(id: String): ArgonautEntity?

    @Query("SELECT * FROM argonauts")
    fun getAllArgonauts(): Flow<List<ArgonautEntity>>

    @Query("SELECT * FROM argonauts WHERE roster_id = :rosterId")
    fun getArgonautsForRoster(rosterId: String): Flow<List<ArgonautEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoster(roster: RosterEntity)

    @Query("SELECT * FROM rosters")
    fun getAllRosters(): Flow<List<RosterEntity>>

    @Query("SELECT * FROM rosters WHERE id = :id")
    suspend fun getRosterById(id: String): RosterEntity?

    @Query("DELETE FROM rosters WHERE id = :id")
    suspend fun deleteRoster(id: String)

    @Query("DELETE FROM argonauts WHERE id = :id")
    suspend fun deleteArgonaut(id: String)

    @Query("SELECT DISTINCT faction_id FROM singularities")
    fun getDownloadedFactionIds(): Flow<List<String>>

    @Query("""
        SELECT DISTINCT 
            s.gamesystem_id AS gamesystem_id, 
            IFNULL(m.displayName, s.gamesystem_name) AS gamesystem_name, 
            IFNULL(m.lastUpdated, 1725300000000) AS last_updated 
        FROM singularities s 
        LEFT JOIN game_system_metadata m ON s.gamesystem_id = m.repoName
    """)
    fun getDownloadedGameSystems(): Flow<List<DownloadedGameSystem>>

    @Query("SELECT DISTINCT faction_id, faction_name, gamesystem_id, gamesystem_name FROM singularities")
    fun getDownloadedFactions(): Flow<List<DownloadedFaction>>

    @Query("""
        SELECT DISTINCT faction_id, faction_name, gamesystem_id, gamesystem_name 
        FROM singularities 
        WHERE (gamesystem_id = :gsId OR gamesystem_id = 'wh40k' OR gamesystem_id = 'wh40k-10th')
        AND xml_tag = 'catalogue'
    """)
    suspend fun getDownloadedCataloguesDirect(gsId: String): List<DownloadedFaction>

    @Query("""
        SELECT DISTINCT faction_id, faction_name, gamesystem_id, gamesystem_name 
        FROM singularities 
        WHERE gamesystem_id = :gsId 
        AND xml_tag = 'catalogue'
    """)
    fun getDownloadedCataloguesForSystem(gsId: String): Flow<List<DownloadedFaction>>

    @Query("""
        SELECT * FROM singularities 
        WHERE gamesystem_id = :gsId AND faction_id = :fId 
        AND (xml_tag LIKE '%' || :tag || '%' OR xml_tag = :tag)
    """)
    fun getSingularitiesByTag(gsId: String, fId: String, tag: String): Flow<List<SingularityEntity>>

    @Query("""
        SELECT DISTINCT * FROM singularities 
        WHERE gamesystem_id = :gsId 
        AND (xml_tag LIKE '%' || :tag || '%' OR xml_tag = :tag)
    """)
    fun getSingularitiesByTagForSystem(gsId: String, tag: String): Flow<List<SingularityEntity>>

    @Query("""
        SELECT DISTINCT * FROM singularities 
        WHERE (gamesystem_id = :gsId OR gamesystem_id LIKE '%' || :gsId || '%' OR :gsId LIKE '%' || gamesystem_id || '%')
        AND (xml_tag = 'selectionEntry' OR xml_tag = 'entryLink' OR xml_tag LIKE '%selection%')
    """)
    fun getRootUnitsForSystem(gsId: String): Flow<List<SingularityEntity>>

    @Query("""
        SELECT DISTINCT * FROM singularities 
        WHERE (gamesystem_id = :gsId OR gamesystem_id LIKE '%' || :gsId || '%' OR :gsId LIKE '%' || gamesystem_id || '%')
        AND (
            :fId = 'ALL' 
            OR faction_id = :fId 
            OR faction_id LIKE '%' || :fId || '%' 
            OR :fId LIKE '%' || faction_id || '%'
            OR faction_name LIKE '%' || :fId || '%'
            OR :fId LIKE '%' || faction_name || '%'
            OR faction_id = 'CORE'
        )
        AND (xml_tag = 'selectionEntry' OR xml_tag = 'entryLink' OR xml_tag LIKE '%selection%')
    """)
    fun getTopLevelEntriesForFaction(gsId: String, fId: String): Flow<List<SingularityEntity>>

    @Query("""
        SELECT DISTINCT * FROM singularities 
        WHERE (gamesystem_id = :gsId OR gamesystem_id LIKE '%' || :gsId || '%' OR :gsId LIKE '%' || gamesystem_id || '%')
        AND (
            :fId = 'ALL' 
            OR faction_id = :fId 
            OR faction_id LIKE '%' || :fId || '%' 
            OR :fId LIKE '%' || faction_id || '%'
            OR faction_name LIKE '%' || :fId || '%'
            OR :fId LIKE '%' || faction_name || '%'
            OR faction_id = 'CORE'
        )
        AND (xml_tag = 'selectionEntry' OR xml_tag = 'entryLink' OR xml_tag LIKE '%selection%')
    """)
    fun getRootUnitsForFaction(gsId: String, fId: String): Flow<List<SingularityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadata(metadata: GameSystemMetadataEntity)

    @Query("UPDATE game_system_metadata SET lastUpdated = :timestamp WHERE repoName = :gsId")
    suspend fun updateLastUpdated(gsId: String, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM game_system_metadata WHERE repoName = :repoName")
    suspend fun getMetadata(repoName: String): GameSystemMetadataEntity?

    @Query("DELETE FROM singularities WHERE gamesystem_id = :gsId")
    suspend fun clearSystemSingularities(gsId: String)

    @Query("DELETE FROM singularity_tags WHERE gamesystem_id = :gsId")
    suspend fun clearSystemTags(gsId: String)

    @Query("DELETE FROM category_links WHERE gamesystem_id = :gsId")
    suspend fun clearSystemCategoryLinks(gsId: String)

    @Query("DELETE FROM costs WHERE gamesystem_id = :gsId")
    suspend fun clearSystemCosts(gsId: String)

    @Query("DELETE FROM characteristics WHERE gamesystem_id = :gsId")
    suspend fun clearSystemCharacteristics(gsId: String)

    @Query("DELETE FROM constraints WHERE gamesystem_id = :gsId")
    suspend fun clearSystemConstraints(gsId: String)

    @Query("DELETE FROM sync_metadata WHERE gamesystem_id = :gsId")
    suspend fun clearSystemSyncMetadata(gsId: String)

    @Query("DELETE FROM game_system_metadata WHERE repoName = :repoName")
    suspend fun deleteSystemMetadata(repoName: String)

    @Transaction
    suspend fun clearGameSystemData(gsId: String) {
        clearSystemSingularities(gsId)
        clearSystemTags(gsId)
        clearSystemCategoryLinks(gsId)
        clearSystemCosts(gsId)
        clearSystemCharacteristics(gsId)
        clearSystemConstraints(gsId)
        clearSystemSyncMetadata(gsId)
        deleteSystemMetadata(gsId)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadata(metadata: SyncMetadataEntity)

    @Query("SELECT * FROM sync_metadata WHERE gamesystem_id = :gsId")
    suspend fun getSyncMetadataForSystem(gsId: String): List<SyncMetadataEntity>

    @Query("SELECT * FROM sync_metadata WHERE gamesystem_id = :gsId AND file_path = :path")
    suspend fun getSyncMetadataByPath(gsId: String, path: String): SyncMetadataEntity?

    @Query("SELECT * FROM sync_metadata WHERE gamesystem_id = :gsId AND faction_id = :fId LIMIT 1")
    suspend fun getSyncMetadataByFaction(gsId: String, fId: String): SyncMetadataEntity?

    @Query("DELETE FROM sync_metadata WHERE gamesystem_id = :gsId AND file_path NOT IN (:activePaths)")
    suspend fun pruneOrphanMetadata(gsId: String, activePaths: List<String>)

    /**
     * Prunes rule data for factions that no longer exist in the repository tree.
     */
    @Query("""
        DELETE FROM singularities 
        WHERE gamesystem_id = :gsId 
        AND faction_id NOT IN (SELECT faction_id FROM sync_metadata WHERE gamesystem_id = :gsId)
        AND faction_id != 'CORE'
    """)
    suspend fun pruneOrphanSingularities(gsId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSingularitiesIgnore(singularities: List<SingularityEntity>): List<Long>

    @Update
    suspend fun updateSingularities(singularities: List<SingularityEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTagsIgnore(tags: List<SingularityTagEntity>): List<Long>

    @Update
    suspend fun updateTags(tags: List<SingularityTagEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryLinksIgnore(links: List<CategoryLinkEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCostsIgnore(costs: List<CostEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacteristicsIgnore(characteristics: List<CharacteristicEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConstraintsIgnore(constraints: List<ConstraintEntity>): List<Long>

    @Query("""
        SELECT * FROM constraints
        WHERE gamesystem_id = :gsId AND faction_id = :fId AND singularity_id = :singularityId
    """)
    fun getConstraintsForSingularity(gsId: String, fId: String, singularityId: String): Flow<List<ConstraintEntity>>

    @Query("""
        SELECT * FROM characteristics
        WHERE gamesystem_id = :gsId AND faction_id = :fId AND singularity_id = :singularityId
    """)
    fun getCharacteristicsForSingularity(gsId: String, fId: String, singularityId: String): Flow<List<CharacteristicEntity>>

    @Query("""
        SELECT * FROM characteristics
        WHERE gamesystem_id = :gsId AND faction_id = :fId AND singularity_id = :singularityId
    """)
    suspend fun getCharacteristicsList(gsId: String, fId: String, singularityId: String): List<CharacteristicEntity>

    @Query("SELECT * FROM singularities WHERE gamesystem_id = :gsId AND id = :id LIMIT 1")
    fun getSingularityByIdFlow(gsId: String, id: String): Flow<SingularityEntity?>

    @Query("""
        SELECT * FROM characteristics
        WHERE gamesystem_id = :gsId AND faction_id = :fId 
        AND (singularity_id = :unitId OR singularity_id IN (SELECT id FROM singularities WHERE parent_id = :unitId))
        AND (profile_type LIKE '%Unit%' OR profile_type LIKE '%Model%')
    """)
    fun getUnitProfilesForUnitAndModels(gsId: String, fId: String, unitId: String): Flow<List<CharacteristicEntity>>

    @Query("""
        SELECT * FROM singularities
        WHERE gamesystem_id = :gsId AND faction_id = :fId 
        AND parent_id = :modelId AND xml_tag = 'selectionEntryGroup'
    """)
    fun getWargearGroupsForModel(gsId: String, fId: String, modelId: String): Flow<List<SingularityEntity>>

    @Query("""
        SELECT * FROM costs
        WHERE gamesystem_id = :gsId AND faction_id = :fId AND singularity_id = :singularityId
    """)
    fun getCostsForSingularity(gsId: String, fId: String, singularityId: String): Flow<List<CostEntity>>

    @Query("""
        SELECT * FROM category_links
        WHERE gamesystem_id = :gsId AND faction_id = :fId AND singularity_id = :singularityId
    """)
    fun getCategoryLinksForSingularity(gsId: String, fId: String, singularityId: String): Flow<List<CategoryLinkEntity>>

    @Query("""
        SELECT category_name FROM category_links
        WHERE gamesystem_id = :gsId AND faction_id = :fId AND singularity_id = :singularityId
    """)
    fun getKeywordsForUnit(gsId: String, fId: String, singularityId: String): Flow<List<String>>

    @Transaction
    suspend fun upsertFactionData(
        gamesystemId: String,
        factionId: String,
        singularities: List<SingularityEntity>,
        tags: List<SingularityTagEntity>
    ) {
        // 1. Safe Upsert: Insert new or Ignore existing
        insertSingularitiesIgnore(singularities)
        insertTagsIgnore(tags)

        // 2. Refresh: Update existing records to new values (Doesn't trigger DELETE/RESTRICT)
        updateSingularities(singularities)
        updateTags(tags)
    }

}
