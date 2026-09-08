package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.battlebarge.agnostic.domain.model.Argonaut

/**
 * Snapshot User Roster Unit Record.
 * Decoupled from strict foreign key RESTRICT on SingularityEntity to allow rulebook updates
 * without throwing SQLiteConstraintException or locking database transactions.
 */
@Entity(
    tableName = "argonauts",
    foreignKeys = [
        ForeignKey(
            entity = RosterEntity::class,
            parentColumns = ["id"],
            childColumns = ["roster_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArgonautEntity::class,
            parentColumns = ["id"],
            childColumns = ["parent_argonaut_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["singularity_id"]),
        Index(value = ["roster_id"]),
        Index(value = ["parent_argonaut_id"]),
        Index(value = ["gamesystem_id", "faction_id"]),
        Index(value = ["gamesystem_id", "faction_id", "singularity_id"])
    ]
)
data class ArgonautEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "roster_id")
    val rosterId: String,
    @ColumnInfo(name = "singularity_id")
    val singularityId: String,
    @ColumnInfo(name = "gamesystem_id")
    val gamesystemId: String,
    @ColumnInfo(name = "faction_id")
    val factionId: String,
    @ColumnInfo(name = "parent_argonaut_id")
    val parentArgonautId: String? = null,
    
    @ColumnInfo(name = "personal_name")
    val personalName: String,
    val designation: String? = null,
    val personality: String? = null,
    @ColumnInfo(name = "image_uri")
    val imageUri: String? = null,
    
    @ColumnInfo(name = "is_warlord")
    val isWarlord: Boolean = false,
    @ColumnInfo(name = "model_count")
    val modelCount: Int = 1,
    
    @ColumnInfo(name = "base_points")
    val basePoints: Int = 0,
    @ColumnInfo(name = "model_cost")
    val modelCost: Int = 0,
    @ColumnInfo(name = "wargear_points")
    val wargearPoints: Int = 0,
    
    @ColumnInfo(name = "rule_sha")
    val ruleSha: String? = null
)

fun ArgonautEntity.toDomain(): Argonaut {
    return Argonaut(
        id = id,
        rosterId = rosterId,
        singularityId = singularityId,
        gamesystemId = gamesystemId,
        factionId = factionId,
        parentArgonautId = parentArgonautId,
        personalName = personalName,
        designation = designation,
        personality = personality,
        imageUri = imageUri,
        isWarlord = isWarlord,
        modelCount = modelCount,
        basePoints = basePoints,
        modelCost = modelCost,
        wargearPoints = wargearPoints,
        ruleSha = ruleSha
    )
}

fun Argonaut.toEntity(): ArgonautEntity {
    return ArgonautEntity(
        id = id,
        rosterId = rosterId,
        singularityId = singularityId,
        gamesystemId = gamesystemId,
        factionId = factionId,
        parentArgonautId = parentArgonautId,
        personalName = personalName,
        designation = designation,
        personality = personality,
        imageUri = imageUri,
        isWarlord = isWarlord,
        modelCount = modelCount,
        basePoints = basePoints,
        modelCost = modelCost,
        wargearPoints = wargearPoints,
        ruleSha = ruleSha
    )
}
