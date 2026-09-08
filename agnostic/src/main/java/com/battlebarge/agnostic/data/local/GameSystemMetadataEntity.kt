package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores universal passport metadata mapping XML GUID <-> Repo Slug <-> Display Name.
 */
@Entity(tableName = "game_system_metadata")
data class GameSystemMetadataEntity(
    @PrimaryKey
    val repoName: String,
    @ColumnInfo(name = "gamesystem_id")
    val gamesystemId: String? = null,
    val displayName: String,
    val owner: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
