package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Stores tags associated with a Singularity.
 * Includes scoping fields to match the composite primary key of SingularityEntity.
 */
@Entity(
    tableName = "singularity_tags",
    primaryKeys = ["gamesystem_id", "faction_id", "singularity_id", "tag"],
    foreignKeys = [
        ForeignKey(
            entity = SingularityEntity::class,
            parentColumns = ["gamesystem_id", "faction_id", "id"],
            childColumns = ["gamesystem_id", "faction_id", "singularity_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["singularity_id"]),
        Index(value = ["tag"]),
        Index(value = ["tag", "singularity_id"]), // Optimized keyword search
        Index(value = ["gamesystem_id", "faction_id"])
    ]
)
data class SingularityTagEntity(
    @ColumnInfo(name = "gamesystem_id")
    val gamesystemId: String,
    @ColumnInfo(name = "faction_id")
    val factionId: String,
    @ColumnInfo(name = "singularity_id")
    val singularityId: String,
    val tag: String
)
