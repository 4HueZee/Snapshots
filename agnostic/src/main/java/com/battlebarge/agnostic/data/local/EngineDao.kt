package com.battlebarge.agnostic.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface EngineDao {

    // --- GAME SYSTEMS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameSystem(system: GameSystemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameSystems(systems: List<GameSystemEntity>)

    @Query("SELECT * FROM game_systems WHERE is_installed = 1")
    fun getInstalledGameSystems(): Flow<List<GameSystemEntity>>

    @Query("SELECT * FROM game_systems WHERE id = :id LIMIT 1")
    suspend fun getGameSystemById(id: String): GameSystemEntity?

    @Query("UPDATE game_systems SET is_installed = 0 WHERE id = :id")
    suspend fun uninstallGameSystem(id: String)

    @Query("DELETE FROM game_systems WHERE id = :id")
    suspend fun deleteGameSystem(id: String)

    // --- FACTIONS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFactions(factions: List<FactionEntity>)

    @Query("SELECT * FROM factions WHERE gamesystem_id = :gsId")
    fun getFactionsForSystem(gsId: String): Flow<List<FactionEntity>>

    @Query("SELECT * FROM factions WHERE gamesystem_id = :gsId")
    suspend fun getFactionsListForSystem(gsId: String): List<FactionEntity>

    // --- DATASHEETS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDatasheets(datasheets: List<UnitDatasheetEntity>)

    @Query("SELECT * FROM unit_datasheets WHERE gamesystem_id = :gsId AND (faction_id = :fId OR :fId = 'ALL')")
    fun getDatasheetsForFaction(gsId: String, fId: String): Flow<List<UnitDatasheetEntity>>

    @Query("SELECT * FROM unit_datasheets WHERE gamesystem_id = :gsId AND (faction_id = :fId OR :fId = 'ALL') AND category = :category")
    fun getDatasheetsForCategory(gsId: String, fId: String, category: String): Flow<List<UnitDatasheetEntity>>

    @Query("SELECT DISTINCT category FROM unit_datasheets WHERE gamesystem_id = :gsId AND (faction_id = :fId OR :fId = 'ALL')")
    fun getCategoriesForFaction(gsId: String, fId: String): Flow<List<String>>

    // --- WEAPON PROFILES ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeaponProfiles(weapons: List<WeaponProfileEntity>)

    @Query("SELECT * FROM weapon_profiles WHERE datasheet_id = :datasheetId")
    fun getWeaponsForDatasheet(datasheetId: String): Flow<List<WeaponProfileEntity>>

    // --- ROSTERS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoster(roster: RosterEntity)

    @Query("SELECT * FROM rosters")
    fun getAllRosters(): Flow<List<RosterEntity>>

    @Query("SELECT * FROM rosters WHERE id = :id LIMIT 1")
    suspend fun getRosterById(id: String): RosterEntity?

    @Query("DELETE FROM rosters WHERE id = :id")
    suspend fun deleteRoster(id: String)

    // --- ROSTER UNITS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRosterUnit(unit: RosterUnitEntity)

    @Query("SELECT * FROM roster_units WHERE roster_id = :rosterId")
    fun getRosterUnits(rosterId: String): Flow<List<RosterUnitEntity>>

    @Query("SELECT * FROM roster_units WHERE id = :id LIMIT 1")
    suspend fun getRosterUnitById(id: String): RosterUnitEntity?

    @Query("DELETE FROM roster_units WHERE id = :id")
    suspend fun deleteRosterUnit(id: String)

    @Transaction
    suspend fun clearSystemData(gsId: String) {
        uninstallGameSystem(gsId)
        deleteGameSystem(gsId)
    }
}
