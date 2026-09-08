package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.battlebarge.agnostic.domain.model.Singularity

/**
 * Room entity representing a Singularity.
 * Refactored for Native XML Relational Architecture (Version 10 Schema).
 */
@Entity(
    tableName = "singularities",
    primaryKeys = ["gamesystem_id", "faction_id", "id"],
    indices = [
        Index(value = ["xml_tag"]),
        Index(value = ["parent_id"]),
        Index(value = ["gamesystem_id"]),
        Index(value = ["faction_id"]),
        Index(value = ["target_id"]),
        Index(value = ["category"]),
        Index(value = ["gamesystem_id", "category"]),
        Index(value = ["gamesystem_id", "target_id"]),
        Index(value = ["parent_id", "id"]),
        Index(value = ["gamesystem_id", "faction_id", "parent_id"])
    ]
)
data class SingularityEntity(
    val id: String,
    @ColumnInfo(name = "gamesystem_id")
    val gamesystemId: String,
    @ColumnInfo(name = "faction_id")
    val factionId: String,
    
    val name: String,
    @ColumnInfo(name = "xml_tag")
    val xmlTag: String,
    @ColumnInfo(name = "entry_type")
    val entryType: String? = null,
    val value: String?,
    @ColumnInfo(name = "parent_id")
    val parentId: String?,
    
    // Relational Link Fields
    @ColumnInfo(name = "target_id")
    val targetId: String? = null,
    @ColumnInfo(name = "link_type")
    val linkType: String? = null,
    
    // Universal Force Org Category
    val category: String? = null,
    
    @ColumnInfo(name = "gamesystem_name")
    val gamesystemName: String,
    @ColumnInfo(name = "faction_name")
    val factionName: String,
    
    @ColumnInfo(name = "is_awakened")
    val isAwakened: Boolean = false
)

data class DownloadedFaction(
    @ColumnInfo(name = "faction_id") val factionId: String,
    @ColumnInfo(name = "faction_name") val factionName: String,
    @ColumnInfo(name = "gamesystem_id") val gamesystemId: String,
    @ColumnInfo(name = "gamesystem_name") val gamesystemName: String
)

data class DownloadedGameSystem(
    @ColumnInfo(name = "gamesystem_id") val id: String,
    @ColumnInfo(name = "gamesystem_name") val name: String,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long = 0L
)

fun SingularityEntity.toDomain(tags: List<String> = emptyList()): Singularity {
    return Singularity(
        id = id,
        name = name,
        xmlTag = xmlTag,
        entryType = entryType,
        value = value,
        parentId = parentId,
        targetId = targetId,
        linkType = linkType,
        category = category,
        tags = tags,
        gamesystemId = gamesystemId,
        gamesystemName = gamesystemName,
        factionId = factionId,
        factionName = factionName,
        isAwakened = isAwakened
    )
}

fun Singularity.toEntity(): SingularityEntity {
    return SingularityEntity(
        id = id,
        name = name,
        xmlTag = xmlTag,
        entryType = entryType,
        value = value,
        parentId = parentId,
        targetId = targetId,
        linkType = linkType,
        category = category,
        gamesystemId = gamesystemId,
        gamesystemName = gamesystemName,
        factionId = factionId,
        factionName = factionName,
        isAwakened = isAwakened
    )
}
