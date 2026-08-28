package com.battlebarge.agnostic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SingularityEntity::class,
        SingularityTagEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class EdenDatabase : RoomDatabase() {
    abstract fun singularityDao(): SingularityDao
}
