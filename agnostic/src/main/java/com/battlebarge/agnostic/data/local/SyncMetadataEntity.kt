package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Entity to track the synchronization state of individual rule files.
 * This enables Differential Sync (Delta Updates) by comparing local SHAs with remote SHAs.
 */
@Entity(
    tableName = "sync_metadata",
    primaryKeys = ["gamesystem_id", "file_path"]
)
data class SyncMetadataEntity(
    @ColumnInfo(name = "file_path")
    val filePath: String,
    
    @ColumnInfo(name = "gamesystem_id")
    val gameSystemId: String,
    
    val sha: String,
    
    @ColumnInfo(name = "faction_id")
    val factionId: String,
    
    @ColumnInfo(name = "last_synced")
    val lastSynced: Long = System.currentTimeMillis()
)
