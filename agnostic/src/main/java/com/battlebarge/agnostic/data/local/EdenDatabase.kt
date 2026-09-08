package com.battlebarge.agnostic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        GameSystemEntity::class,
        FactionEntity::class,
        UnitDatasheetEntity::class,
        WeaponProfileEntity::class,
        RosterEntity::class,
        RosterUnitEntity::class,
        SingularityEntity::class,
        SingularityTagEntity::class,
        CategoryLinkEntity::class,
        CostEntity::class,
        CharacteristicEntity::class,
        ConstraintEntity::class,
        ArgonautEntity::class,
        GameSystemMetadataEntity::class,
        SyncMetadataEntity::class
    ],
    version = 16, // Incremented for Strict Faction Numerical/GUID Isolation & Clean Reset
    exportSchema = true
)
abstract class EdenDatabase : RoomDatabase() {
    abstract fun engineDao(): EngineDao
    abstract fun singularityDao(): SingularityDao
}
