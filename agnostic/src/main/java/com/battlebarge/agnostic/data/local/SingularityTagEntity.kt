package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Stores tags associated with a Singularity.
 * This is a separate table to allow many-to-one mapping (One Singularity, many tags).
 */
@Entity(
    tableName = "singularity_tags",
    primaryKeys = ["singularity_id", "tag"],
    foreignKeys = [
        ForeignKey(
            entity = SingularityEntity::class,
            parentColumns = ["id"],
            childColumns = ["singularity_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["singularity_id"]),
        Index(value = ["tag"])
    ]
)
data class SingularityTagEntity(
    @ColumnInfo(name = "singularity_id")
    val singularityId: String,
    val tag: String
)
