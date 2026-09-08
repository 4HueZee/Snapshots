package com.battlebarge.agnostic.domain.repository

import com.battlebarge.agnostic.data.local.EdenDatabase
import com.battlebarge.agnostic.data.local.RosterEntity
import com.battlebarge.agnostic.data.local.toDomain
import com.battlebarge.agnostic.data.local.toEntity
import com.battlebarge.agnostic.domain.model.Argonaut
import com.battlebarge.agnostic.domain.model.Singularity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Handles the creation and management of Argonaut records and Rosters.
 */
class ArgonautRepository(
    private val database: EdenDatabase
) {
    private val dao = database.singularityDao()

    fun getAllRosters(): Flow<List<RosterEntity>> = dao.getAllRosters()

    suspend fun createRoster(name: String, gamesystemId: String, factionId: String): RosterEntity {
        val roster = RosterEntity(
            name = name,
            gamesystemId = gamesystemId,
            factionId = factionId
        )
        dao.insertRoster(roster)
        return roster
    }

    suspend fun renameRoster(id: String, newName: String) {
        val roster = dao.getRosterById(id)
        if (roster != null) {
            dao.insertRoster(roster.copy(name = newName))
        }
    }

    suspend fun deleteRoster(id: String) = dao.deleteRoster(id)

    /**
     * Erases all user-created rosters and units.
     */
    suspend fun clearAllUserData(): Result<Unit> {
        return try {
            dao.clearUserDatabase()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getArgonautsForRoster(rosterId: String): Flow<List<Argonaut>> {
        return dao.getArgonautsForRoster(rosterId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getAllArgonauts(): Flow<List<Argonaut>> {
        return dao.getAllArgonauts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getArgonaut(id: String): Argonaut? {
        return dao.getArgonautById(id)?.toDomain()
    }

    suspend fun updateArgonaut(argonaut: Argonaut) {
        dao.insertArgonaut(argonaut.toEntity())
    }

    suspend fun deleteArgonaut(id: String) {
        dao.deleteArgonaut(id)
    }

    /**
     * Toggles Warlord designation for an Argonaut, enforcing that only Character/HQ eligible units can become Warlord in SQLite.
     */
    suspend fun toggleWarlord(argonaut: Argonaut) {
        val baseRule = dao.getSingularityById(argonaut.gamesystemId, argonaut.factionId, argonaut.singularityId)
        val category = baseRule?.category?.uppercase() ?: ""
        val name = (baseRule?.name ?: argonaut.personalName).uppercase()

        val isWarlordEligible = category == "CHARACTERS" || 
                name.contains("CAPTAIN") || name.contains("COMMISSAR") || name.contains("LIBRARIAN") || 
                name.contains("CHAPLAIN") || name.contains("CANONESS") || name.contains("PALATINE") || 
                name.contains("CHARACTER") || name.contains("HERO") || name.contains("WARLORD") || name.contains("HQ")

        if (!isWarlordEligible) {
            // Standard non-Character units cannot hold Warlord status in SQLite
            return
        }

        val newWarlordStatus = !argonaut.isWarlord
        dao.insertArgonaut(argonaut.toEntity().copy(isWarlord = newWarlordStatus))
    }

    /**
     * Updates model count / squad size for an Argonaut.
     */
    suspend fun updateModelCount(argonautId: String, newCount: Int) {
        val entity = dao.getArgonautById(argonautId)
        if (entity != null) {
            val clampedCount = newCount.coerceAtLeast(1)
            dao.insertArgonaut(entity.copy(modelCount = clampedCount))
        }
    }

    /**
     * Creates a deep copy of an Argonaut.
     */
    suspend fun duplicate(argonaut: Argonaut): Argonaut {
        val copy = argonaut.copy(
            id = UUID.randomUUID().toString(),
            personalName = "${argonaut.personalName} (Copy)",
            isWarlord = false
        )
        dao.insertArgonaut(copy.toEntity())
        return copy
    }

    /**
     * Updates an Argonaut's rule SHA to the latest available to clear "Outdated" status.
     */
    suspend fun updateToLatestRules(argonaut: Argonaut): Result<Unit> {
        val metadata = dao.getSyncMetadataByFaction(argonaut.gamesystemId, argonaut.factionId)
            ?: return Result.failure(Exception("Rules not found for faction"))
            
        val updated = argonaut.copy(ruleSha = metadata.sha)
        updateArgonaut(updated)
        return Result.success(Unit)
    }

    /**
     * Atomically swaps an equipped wargear child Argonaut with a new selection.
     */
    suspend fun swapWargear(
        unitArgonautId: String,
        oldWargearId: String?,
        newSingularity: Singularity
    ) {
        if (oldWargearId != null) {
            dao.deleteArgonaut(oldWargearId)
        }

        val unitEntity = dao.getArgonautById(unitArgonautId) ?: return
        val metadata = dao.getSyncMetadataByFaction(unitEntity.gamesystemId, unitEntity.factionId)
        val wargearCost = newSingularity.value?.toIntOrNull() ?: 5

        val childArgonaut = Argonaut(
            id = UUID.randomUUID().toString(),
            rosterId = unitEntity.rosterId,
            singularityId = newSingularity.id,
            gamesystemId = newSingularity.gamesystemId,
            factionId = newSingularity.factionId,
            parentArgonautId = unitArgonautId,
            personalName = newSingularity.name,
            basePoints = wargearCost,
            ruleSha = metadata?.sha
        )
        dao.insertArgonaut(childArgonaut.toEntity())
    }

    /**
     * Toggles optional equipment on or off for a unit Argonaut.
     */
    suspend fun toggleOptionalWargear(
        unitArgonautId: String,
        wargearSingularity: Singularity,
        isEquipped: Boolean
    ) {
        val unitEntity = dao.getArgonautById(unitArgonautId) ?: return
        
        if (isEquipped) {
            val metadata = dao.getSyncMetadataByFaction(unitEntity.gamesystemId, unitEntity.factionId)
            val wargearCost = wargearSingularity.value?.toIntOrNull() ?: 5

            val childArgonaut = Argonaut(
                id = UUID.randomUUID().toString(),
                rosterId = unitEntity.rosterId,
                singularityId = wargearSingularity.id,
                gamesystemId = wargearSingularity.gamesystemId,
                factionId = wargearSingularity.factionId,
                parentArgonautId = unitArgonautId,
                personalName = wargearSingularity.name,
                basePoints = wargearCost,
                ruleSha = metadata?.sha
            )
            dao.insertArgonaut(childArgonaut.toEntity())
        }
    }

    /**
     * "Awakens" a base rule into a living record within a specific roster.
     */
    suspend fun awaken(
        singularity: Singularity,
        rosterId: String,
        personalName: String,
        designation: String? = null,
        personality: String? = null,
        imageUri: String? = null
    ): Argonaut {
        // Fetch current SHA from SyncMetadata to enable Delta Awareness
        val metadata = dao.getSyncMetadataByFaction(singularity.gamesystemId, singularity.factionId)
        val parsedPoints = singularity.value?.toIntOrNull() ?: 85

        val argonaut = Argonaut(
            singularityId = singularity.id,
            rosterId = rosterId,
            gamesystemId = singularity.gamesystemId,
            factionId = singularity.factionId,
            personalName = personalName,
            designation = designation,
            personality = personality,
            imageUri = imageUri,
            basePoints = parsedPoints,
            ruleSha = metadata?.sha
        )
        dao.insertArgonaut(argonaut.toEntity())
        return argonaut
    }

    /**
     * "Awakens" a Unit and all its default child wargear into a living record hierarchy within a roster.
     */
    suspend fun awakenUnitWithWargear(
        singularity: Singularity,
        rosterId: String,
        personalName: String,
        designation: String? = null,
        personality: String? = null,
        imageUri: String? = null
    ): Argonaut {
        // 1. Awaken Root Unit (parentArgonautId = null)
        val rootArgonaut = awaken(
            singularity = singularity,
            rosterId = rosterId,
            personalName = personalName,
            designation = designation,
            personality = personality,
            imageUri = imageUri
        )

        // 2. Fetch direct child Singularities (default wargear/models)
        val childEntities = dao.getChildrenList(
            singularity.gamesystemId,
            singularity.factionId,
            singularity.id
        )

        val metadata = dao.getSyncMetadataByFaction(singularity.gamesystemId, singularity.factionId)

        // 3. Awaken child default wargear under the root Unit
        childEntities.forEach { childEntity ->
            val childArgonaut = Argonaut(
                id = UUID.randomUUID().toString(),
                rosterId = rosterId,
                singularityId = childEntity.id,
                gamesystemId = childEntity.gamesystemId,
                factionId = childEntity.factionId,
                parentArgonautId = rootArgonaut.id,
                personalName = childEntity.name,
                designation = null,
                personality = null,
                imageUri = null,
                ruleSha = metadata?.sha
            )
            dao.insertArgonaut(childArgonaut.toEntity())
        }

        return rootArgonaut
    }

    /**
     * Resolves the Force Org Category for a Singularity (Unit).
     */
    suspend fun resolveUnitCategory(singularity: Singularity): String {
        val tags = dao.getTagsForSingularity(singularity.gamesystemId, singularity.factionId, singularity.id)
        val allDescriptors = (tags + singularity.xmlTag + singularity.name).map { it.uppercase() }

        return when {
            allDescriptors.any { it.contains("CHARACTER") || it.contains("HQ") || it.contains("HERO") || it.contains("WARLORD") } -> "CHARACTERS"
            allDescriptors.any { it.contains("BATTLELINE") || it.contains("TROOPS") || it.contains("CORE") } -> "BATTLELINE"
            allDescriptors.any { it.contains("TRANSPORT") } -> "DEDICATED TRANSPORTS"
            else -> "OTHER UNITS"
        }
    }
}
