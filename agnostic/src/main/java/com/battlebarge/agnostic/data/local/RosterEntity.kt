package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Technical Name: RosterEntity (The Army List).
 * Groups individual units (Argonauts) together into a single collection.
 */
@Entity(tableName = "rosters")
data class RosterEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @ColumnInfo(name = "gamesystem_id")
    val gamesystemId: String,
    @ColumnInfo(name = "faction_id")
    val factionId: String,
    @ColumnInfo(name = "points_limit")
    val pointsLimit: Int = 2000,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
