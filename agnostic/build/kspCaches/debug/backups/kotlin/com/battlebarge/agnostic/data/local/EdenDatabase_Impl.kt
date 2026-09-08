package com.battlebarge.agnostic.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class EdenDatabase_Impl : EdenDatabase() {
  private val _engineDao: Lazy<EngineDao> = lazy {
    EngineDao_Impl(this)
  }

  private val _singularityDao: Lazy<SingularityDao> = lazy {
    SingularityDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(16, "3cfc537ac0a6b1613b7e8a11c7280976", "d1639ea921ba9e022c8ebbbc89b10a4e") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `game_systems` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `owner` TEXT NOT NULL, `repo_name` TEXT NOT NULL, `default_branch` TEXT NOT NULL, `last_updated` INTEGER NOT NULL, `is_installed` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `factions` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `gamesystem_id` TEXT NOT NULL, `gamesystem_name` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_factions_gamesystem_id` ON `factions` (`gamesystem_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `unit_datasheets` (`id` TEXT NOT NULL, `gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `name` TEXT NOT NULL, `category` TEXT NOT NULL, `base_points` INTEGER NOT NULL, `movement` TEXT NOT NULL, `toughness` TEXT NOT NULL, `save` TEXT NOT NULL, `wounds` TEXT NOT NULL, `leadership` TEXT NOT NULL, `objective_control` TEXT NOT NULL, `keywords` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unit_datasheets_gamesystem_id` ON `unit_datasheets` (`gamesystem_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unit_datasheets_faction_id` ON `unit_datasheets` (`faction_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unit_datasheets_gamesystem_id_faction_id` ON `unit_datasheets` (`gamesystem_id`, `faction_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unit_datasheets_category` ON `unit_datasheets` (`category`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `weapon_profiles` (`id` TEXT NOT NULL, `datasheet_id` TEXT NOT NULL, `gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `name` TEXT NOT NULL, `range` TEXT NOT NULL, `attacks` TEXT NOT NULL, `skill` TEXT NOT NULL, `strength` TEXT NOT NULL, `ap` TEXT NOT NULL, `damage` TEXT NOT NULL, `is_ranged` INTEGER NOT NULL, `keywords` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_weapon_profiles_datasheet_id` ON `weapon_profiles` (`datasheet_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_weapon_profiles_gamesystem_id_faction_id` ON `weapon_profiles` (`gamesystem_id`, `faction_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `rosters` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `points_limit` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `roster_units` (`id` TEXT NOT NULL, `roster_id` TEXT NOT NULL, `datasheet_id` TEXT NOT NULL, `gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `parent_roster_unit_id` TEXT, `personal_name` TEXT NOT NULL, `model_count` INTEGER NOT NULL, `points` INTEGER NOT NULL, `is_warlord` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_roster_units_roster_id` ON `roster_units` (`roster_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_roster_units_datasheet_id` ON `roster_units` (`datasheet_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `singularities` (`id` TEXT NOT NULL, `gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `name` TEXT NOT NULL, `xml_tag` TEXT NOT NULL, `entry_type` TEXT, `value` TEXT, `parent_id` TEXT, `target_id` TEXT, `link_type` TEXT, `category` TEXT, `gamesystem_name` TEXT NOT NULL, `faction_name` TEXT NOT NULL, `is_awakened` INTEGER NOT NULL, PRIMARY KEY(`gamesystem_id`, `faction_id`, `id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_xml_tag` ON `singularities` (`xml_tag`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_parent_id` ON `singularities` (`parent_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_gamesystem_id` ON `singularities` (`gamesystem_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_faction_id` ON `singularities` (`faction_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_target_id` ON `singularities` (`target_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_category` ON `singularities` (`category`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_gamesystem_id_category` ON `singularities` (`gamesystem_id`, `category`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_gamesystem_id_target_id` ON `singularities` (`gamesystem_id`, `target_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_parent_id_id` ON `singularities` (`parent_id`, `id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_gamesystem_id_faction_id_parent_id` ON `singularities` (`gamesystem_id`, `faction_id`, `parent_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `singularity_tags` (`gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `singularity_id` TEXT NOT NULL, `tag` TEXT NOT NULL, PRIMARY KEY(`gamesystem_id`, `faction_id`, `singularity_id`, `tag`), FOREIGN KEY(`gamesystem_id`, `faction_id`, `singularity_id`) REFERENCES `singularities`(`gamesystem_id`, `faction_id`, `id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularity_tags_singularity_id` ON `singularity_tags` (`singularity_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularity_tags_tag` ON `singularity_tags` (`tag`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularity_tags_tag_singularity_id` ON `singularity_tags` (`tag`, `singularity_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularity_tags_gamesystem_id_faction_id` ON `singularity_tags` (`gamesystem_id`, `faction_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `category_links` (`gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `singularity_id` TEXT NOT NULL, `target_id` TEXT NOT NULL, `category_name` TEXT NOT NULL, `is_primary` INTEGER NOT NULL, PRIMARY KEY(`gamesystem_id`, `faction_id`, `singularity_id`, `target_id`), FOREIGN KEY(`gamesystem_id`, `faction_id`, `singularity_id`) REFERENCES `singularities`(`gamesystem_id`, `faction_id`, `id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_category_links_singularity_id` ON `category_links` (`singularity_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_category_links_target_id` ON `category_links` (`target_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_category_links_category_name` ON `category_links` (`category_name`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `costs` (`gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `singularity_id` TEXT NOT NULL, `name` TEXT NOT NULL, `value` REAL NOT NULL, PRIMARY KEY(`gamesystem_id`, `faction_id`, `singularity_id`, `name`), FOREIGN KEY(`gamesystem_id`, `faction_id`, `singularity_id`) REFERENCES `singularities`(`gamesystem_id`, `faction_id`, `id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_costs_singularity_id` ON `costs` (`singularity_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_costs_name` ON `costs` (`name`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `characteristics` (`gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `singularity_id` TEXT NOT NULL, `profile_name` TEXT NOT NULL, `profile_type` TEXT NOT NULL, `stat_name` TEXT NOT NULL, `stat_value` TEXT NOT NULL, PRIMARY KEY(`gamesystem_id`, `faction_id`, `singularity_id`, `profile_name`, `stat_name`), FOREIGN KEY(`gamesystem_id`, `faction_id`, `singularity_id`) REFERENCES `singularities`(`gamesystem_id`, `faction_id`, `id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_characteristics_singularity_id` ON `characteristics` (`singularity_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_characteristics_profile_name` ON `characteristics` (`profile_name`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_characteristics_stat_name` ON `characteristics` (`stat_name`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `constraints` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `singularity_id` TEXT NOT NULL, `type` TEXT NOT NULL, `value` INTEGER NOT NULL, `field` TEXT, `scope` TEXT)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_constraints_gamesystem_id_faction_id_singularity_id` ON `constraints` (`gamesystem_id`, `faction_id`, `singularity_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_constraints_singularity_id` ON `constraints` (`singularity_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `argonauts` (`id` TEXT NOT NULL, `roster_id` TEXT NOT NULL, `singularity_id` TEXT NOT NULL, `gamesystem_id` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `parent_argonaut_id` TEXT, `personal_name` TEXT NOT NULL, `designation` TEXT, `personality` TEXT, `image_uri` TEXT, `is_warlord` INTEGER NOT NULL, `model_count` INTEGER NOT NULL, `base_points` INTEGER NOT NULL, `model_cost` INTEGER NOT NULL, `wargear_points` INTEGER NOT NULL, `rule_sha` TEXT, PRIMARY KEY(`id`), FOREIGN KEY(`roster_id`) REFERENCES `rosters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`parent_argonaut_id`) REFERENCES `argonauts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_argonauts_singularity_id` ON `argonauts` (`singularity_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_argonauts_roster_id` ON `argonauts` (`roster_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_argonauts_parent_argonaut_id` ON `argonauts` (`parent_argonaut_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_argonauts_gamesystem_id_faction_id` ON `argonauts` (`gamesystem_id`, `faction_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_argonauts_gamesystem_id_faction_id_singularity_id` ON `argonauts` (`gamesystem_id`, `faction_id`, `singularity_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `game_system_metadata` (`repoName` TEXT NOT NULL, `gamesystem_id` TEXT, `displayName` TEXT NOT NULL, `owner` TEXT NOT NULL, `lastUpdated` INTEGER NOT NULL, PRIMARY KEY(`repoName`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sync_metadata` (`file_path` TEXT NOT NULL, `gamesystem_id` TEXT NOT NULL, `sha` TEXT NOT NULL, `faction_id` TEXT NOT NULL, `last_synced` INTEGER NOT NULL, PRIMARY KEY(`gamesystem_id`, `file_path`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3cfc537ac0a6b1613b7e8a11c7280976')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `game_systems`")
        connection.execSQL("DROP TABLE IF EXISTS `factions`")
        connection.execSQL("DROP TABLE IF EXISTS `unit_datasheets`")
        connection.execSQL("DROP TABLE IF EXISTS `weapon_profiles`")
        connection.execSQL("DROP TABLE IF EXISTS `rosters`")
        connection.execSQL("DROP TABLE IF EXISTS `roster_units`")
        connection.execSQL("DROP TABLE IF EXISTS `singularities`")
        connection.execSQL("DROP TABLE IF EXISTS `singularity_tags`")
        connection.execSQL("DROP TABLE IF EXISTS `category_links`")
        connection.execSQL("DROP TABLE IF EXISTS `costs`")
        connection.execSQL("DROP TABLE IF EXISTS `characteristics`")
        connection.execSQL("DROP TABLE IF EXISTS `constraints`")
        connection.execSQL("DROP TABLE IF EXISTS `argonauts`")
        connection.execSQL("DROP TABLE IF EXISTS `game_system_metadata`")
        connection.execSQL("DROP TABLE IF EXISTS `sync_metadata`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsGameSystems: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGameSystems.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystems.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystems.put("owner", TableInfo.Column("owner", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystems.put("repo_name", TableInfo.Column("repo_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystems.put("default_branch", TableInfo.Column("default_branch", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystems.put("last_updated", TableInfo.Column("last_updated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystems.put("is_installed", TableInfo.Column("is_installed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGameSystems: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesGameSystems: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoGameSystems: TableInfo = TableInfo("game_systems", _columnsGameSystems, _foreignKeysGameSystems, _indicesGameSystems)
        val _existingGameSystems: TableInfo = read(connection, "game_systems")
        if (!_infoGameSystems.equals(_existingGameSystems)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |game_systems(com.battlebarge.agnostic.data.local.GameSystemEntity).
              | Expected:
              |""".trimMargin() + _infoGameSystems + """
              |
              | Found:
              |""".trimMargin() + _existingGameSystems)
        }
        val _columnsFactions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFactions.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactions.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactions.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFactions.put("gamesystem_name", TableInfo.Column("gamesystem_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFactions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFactions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesFactions.add(TableInfo.Index("index_factions_gamesystem_id", false, listOf("gamesystem_id"), listOf("ASC")))
        val _infoFactions: TableInfo = TableInfo("factions", _columnsFactions, _foreignKeysFactions, _indicesFactions)
        val _existingFactions: TableInfo = read(connection, "factions")
        if (!_infoFactions.equals(_existingFactions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |factions(com.battlebarge.agnostic.data.local.FactionEntity).
              | Expected:
              |""".trimMargin() + _infoFactions + """
              |
              | Found:
              |""".trimMargin() + _existingFactions)
        }
        val _columnsUnitDatasheets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUnitDatasheets.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("category", TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("base_points", TableInfo.Column("base_points", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("movement", TableInfo.Column("movement", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("toughness", TableInfo.Column("toughness", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("save", TableInfo.Column("save", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("wounds", TableInfo.Column("wounds", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("leadership", TableInfo.Column("leadership", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("objective_control", TableInfo.Column("objective_control", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnitDatasheets.put("keywords", TableInfo.Column("keywords", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUnitDatasheets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUnitDatasheets: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesUnitDatasheets.add(TableInfo.Index("index_unit_datasheets_gamesystem_id", false, listOf("gamesystem_id"), listOf("ASC")))
        _indicesUnitDatasheets.add(TableInfo.Index("index_unit_datasheets_faction_id", false, listOf("faction_id"), listOf("ASC")))
        _indicesUnitDatasheets.add(TableInfo.Index("index_unit_datasheets_gamesystem_id_faction_id", false, listOf("gamesystem_id", "faction_id"), listOf("ASC", "ASC")))
        _indicesUnitDatasheets.add(TableInfo.Index("index_unit_datasheets_category", false, listOf("category"), listOf("ASC")))
        val _infoUnitDatasheets: TableInfo = TableInfo("unit_datasheets", _columnsUnitDatasheets, _foreignKeysUnitDatasheets, _indicesUnitDatasheets)
        val _existingUnitDatasheets: TableInfo = read(connection, "unit_datasheets")
        if (!_infoUnitDatasheets.equals(_existingUnitDatasheets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |unit_datasheets(com.battlebarge.agnostic.data.local.UnitDatasheetEntity).
              | Expected:
              |""".trimMargin() + _infoUnitDatasheets + """
              |
              | Found:
              |""".trimMargin() + _existingUnitDatasheets)
        }
        val _columnsWeaponProfiles: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsWeaponProfiles.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("datasheet_id", TableInfo.Column("datasheet_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("range", TableInfo.Column("range", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("attacks", TableInfo.Column("attacks", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("skill", TableInfo.Column("skill", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("strength", TableInfo.Column("strength", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("ap", TableInfo.Column("ap", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("damage", TableInfo.Column("damage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("is_ranged", TableInfo.Column("is_ranged", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsWeaponProfiles.put("keywords", TableInfo.Column("keywords", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysWeaponProfiles: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesWeaponProfiles: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesWeaponProfiles.add(TableInfo.Index("index_weapon_profiles_datasheet_id", false, listOf("datasheet_id"), listOf("ASC")))
        _indicesWeaponProfiles.add(TableInfo.Index("index_weapon_profiles_gamesystem_id_faction_id", false, listOf("gamesystem_id", "faction_id"), listOf("ASC", "ASC")))
        val _infoWeaponProfiles: TableInfo = TableInfo("weapon_profiles", _columnsWeaponProfiles, _foreignKeysWeaponProfiles, _indicesWeaponProfiles)
        val _existingWeaponProfiles: TableInfo = read(connection, "weapon_profiles")
        if (!_infoWeaponProfiles.equals(_existingWeaponProfiles)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |weapon_profiles(com.battlebarge.agnostic.data.local.WeaponProfileEntity).
              | Expected:
              |""".trimMargin() + _infoWeaponProfiles + """
              |
              | Found:
              |""".trimMargin() + _existingWeaponProfiles)
        }
        val _columnsRosters: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsRosters.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosters.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosters.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosters.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosters.put("points_limit", TableInfo.Column("points_limit", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosters.put("created_at", TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysRosters: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesRosters: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoRosters: TableInfo = TableInfo("rosters", _columnsRosters, _foreignKeysRosters, _indicesRosters)
        val _existingRosters: TableInfo = read(connection, "rosters")
        if (!_infoRosters.equals(_existingRosters)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |rosters(com.battlebarge.agnostic.data.local.RosterEntity).
              | Expected:
              |""".trimMargin() + _infoRosters + """
              |
              | Found:
              |""".trimMargin() + _existingRosters)
        }
        val _columnsRosterUnits: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsRosterUnits.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("roster_id", TableInfo.Column("roster_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("datasheet_id", TableInfo.Column("datasheet_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("parent_roster_unit_id", TableInfo.Column("parent_roster_unit_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("personal_name", TableInfo.Column("personal_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("model_count", TableInfo.Column("model_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("points", TableInfo.Column("points", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRosterUnits.put("is_warlord", TableInfo.Column("is_warlord", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysRosterUnits: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesRosterUnits: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesRosterUnits.add(TableInfo.Index("index_roster_units_roster_id", false, listOf("roster_id"), listOf("ASC")))
        _indicesRosterUnits.add(TableInfo.Index("index_roster_units_datasheet_id", false, listOf("datasheet_id"), listOf("ASC")))
        val _infoRosterUnits: TableInfo = TableInfo("roster_units", _columnsRosterUnits, _foreignKeysRosterUnits, _indicesRosterUnits)
        val _existingRosterUnits: TableInfo = read(connection, "roster_units")
        if (!_infoRosterUnits.equals(_existingRosterUnits)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |roster_units(com.battlebarge.agnostic.data.local.RosterUnitEntity).
              | Expected:
              |""".trimMargin() + _infoRosterUnits + """
              |
              | Found:
              |""".trimMargin() + _existingRosterUnits)
        }
        val _columnsSingularities: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSingularities.put("id", TableInfo.Column("id", "TEXT", true, 3, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("xml_tag", TableInfo.Column("xml_tag", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("entry_type", TableInfo.Column("entry_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("value", TableInfo.Column("value", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("parent_id", TableInfo.Column("parent_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("target_id", TableInfo.Column("target_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("link_type", TableInfo.Column("link_type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("category", TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("gamesystem_name", TableInfo.Column("gamesystem_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("faction_name", TableInfo.Column("faction_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("is_awakened", TableInfo.Column("is_awakened", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSingularities: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSingularities: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSingularities.add(TableInfo.Index("index_singularities_xml_tag", false, listOf("xml_tag"), listOf("ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_parent_id", false, listOf("parent_id"), listOf("ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_gamesystem_id", false, listOf("gamesystem_id"), listOf("ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_faction_id", false, listOf("faction_id"), listOf("ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_target_id", false, listOf("target_id"), listOf("ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_category", false, listOf("category"), listOf("ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_gamesystem_id_category", false, listOf("gamesystem_id", "category"), listOf("ASC", "ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_gamesystem_id_target_id", false, listOf("gamesystem_id", "target_id"), listOf("ASC", "ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_parent_id_id", false, listOf("parent_id", "id"), listOf("ASC", "ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_gamesystem_id_faction_id_parent_id", false, listOf("gamesystem_id", "faction_id", "parent_id"), listOf("ASC", "ASC", "ASC")))
        val _infoSingularities: TableInfo = TableInfo("singularities", _columnsSingularities, _foreignKeysSingularities, _indicesSingularities)
        val _existingSingularities: TableInfo = read(connection, "singularities")
        if (!_infoSingularities.equals(_existingSingularities)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |singularities(com.battlebarge.agnostic.data.local.SingularityEntity).
              | Expected:
              |""".trimMargin() + _infoSingularities + """
              |
              | Found:
              |""".trimMargin() + _existingSingularities)
        }
        val _columnsSingularityTags: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSingularityTags.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularityTags.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularityTags.put("singularity_id", TableInfo.Column("singularity_id", "TEXT", true, 3, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularityTags.put("tag", TableInfo.Column("tag", "TEXT", true, 4, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSingularityTags: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysSingularityTags.add(TableInfo.ForeignKey("singularities", "CASCADE", "NO ACTION", listOf("gamesystem_id", "faction_id", "singularity_id"), listOf("gamesystem_id", "faction_id", "id")))
        val _indicesSingularityTags: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSingularityTags.add(TableInfo.Index("index_singularity_tags_singularity_id", false, listOf("singularity_id"), listOf("ASC")))
        _indicesSingularityTags.add(TableInfo.Index("index_singularity_tags_tag", false, listOf("tag"), listOf("ASC")))
        _indicesSingularityTags.add(TableInfo.Index("index_singularity_tags_tag_singularity_id", false, listOf("tag", "singularity_id"), listOf("ASC", "ASC")))
        _indicesSingularityTags.add(TableInfo.Index("index_singularity_tags_gamesystem_id_faction_id", false, listOf("gamesystem_id", "faction_id"), listOf("ASC", "ASC")))
        val _infoSingularityTags: TableInfo = TableInfo("singularity_tags", _columnsSingularityTags, _foreignKeysSingularityTags, _indicesSingularityTags)
        val _existingSingularityTags: TableInfo = read(connection, "singularity_tags")
        if (!_infoSingularityTags.equals(_existingSingularityTags)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |singularity_tags(com.battlebarge.agnostic.data.local.SingularityTagEntity).
              | Expected:
              |""".trimMargin() + _infoSingularityTags + """
              |
              | Found:
              |""".trimMargin() + _existingSingularityTags)
        }
        val _columnsCategoryLinks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCategoryLinks.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryLinks.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryLinks.put("singularity_id", TableInfo.Column("singularity_id", "TEXT", true, 3, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryLinks.put("target_id", TableInfo.Column("target_id", "TEXT", true, 4, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryLinks.put("category_name", TableInfo.Column("category_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategoryLinks.put("is_primary", TableInfo.Column("is_primary", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCategoryLinks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysCategoryLinks.add(TableInfo.ForeignKey("singularities", "CASCADE", "NO ACTION", listOf("gamesystem_id", "faction_id", "singularity_id"), listOf("gamesystem_id", "faction_id", "id")))
        val _indicesCategoryLinks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCategoryLinks.add(TableInfo.Index("index_category_links_singularity_id", false, listOf("singularity_id"), listOf("ASC")))
        _indicesCategoryLinks.add(TableInfo.Index("index_category_links_target_id", false, listOf("target_id"), listOf("ASC")))
        _indicesCategoryLinks.add(TableInfo.Index("index_category_links_category_name", false, listOf("category_name"), listOf("ASC")))
        val _infoCategoryLinks: TableInfo = TableInfo("category_links", _columnsCategoryLinks, _foreignKeysCategoryLinks, _indicesCategoryLinks)
        val _existingCategoryLinks: TableInfo = read(connection, "category_links")
        if (!_infoCategoryLinks.equals(_existingCategoryLinks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |category_links(com.battlebarge.agnostic.data.local.CategoryLinkEntity).
              | Expected:
              |""".trimMargin() + _infoCategoryLinks + """
              |
              | Found:
              |""".trimMargin() + _existingCategoryLinks)
        }
        val _columnsCosts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCosts.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCosts.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCosts.put("singularity_id", TableInfo.Column("singularity_id", "TEXT", true, 3, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCosts.put("name", TableInfo.Column("name", "TEXT", true, 4, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCosts.put("value", TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCosts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysCosts.add(TableInfo.ForeignKey("singularities", "CASCADE", "NO ACTION", listOf("gamesystem_id", "faction_id", "singularity_id"), listOf("gamesystem_id", "faction_id", "id")))
        val _indicesCosts: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCosts.add(TableInfo.Index("index_costs_singularity_id", false, listOf("singularity_id"), listOf("ASC")))
        _indicesCosts.add(TableInfo.Index("index_costs_name", false, listOf("name"), listOf("ASC")))
        val _infoCosts: TableInfo = TableInfo("costs", _columnsCosts, _foreignKeysCosts, _indicesCosts)
        val _existingCosts: TableInfo = read(connection, "costs")
        if (!_infoCosts.equals(_existingCosts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |costs(com.battlebarge.agnostic.data.local.CostEntity).
              | Expected:
              |""".trimMargin() + _infoCosts + """
              |
              | Found:
              |""".trimMargin() + _existingCosts)
        }
        val _columnsCharacteristics: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCharacteristics.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacteristics.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacteristics.put("singularity_id", TableInfo.Column("singularity_id", "TEXT", true, 3, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacteristics.put("profile_name", TableInfo.Column("profile_name", "TEXT", true, 4, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacteristics.put("profile_type", TableInfo.Column("profile_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacteristics.put("stat_name", TableInfo.Column("stat_name", "TEXT", true, 5, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCharacteristics.put("stat_value", TableInfo.Column("stat_value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCharacteristics: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysCharacteristics.add(TableInfo.ForeignKey("singularities", "CASCADE", "NO ACTION", listOf("gamesystem_id", "faction_id", "singularity_id"), listOf("gamesystem_id", "faction_id", "id")))
        val _indicesCharacteristics: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCharacteristics.add(TableInfo.Index("index_characteristics_singularity_id", false, listOf("singularity_id"), listOf("ASC")))
        _indicesCharacteristics.add(TableInfo.Index("index_characteristics_profile_name", false, listOf("profile_name"), listOf("ASC")))
        _indicesCharacteristics.add(TableInfo.Index("index_characteristics_stat_name", false, listOf("stat_name"), listOf("ASC")))
        val _infoCharacteristics: TableInfo = TableInfo("characteristics", _columnsCharacteristics, _foreignKeysCharacteristics, _indicesCharacteristics)
        val _existingCharacteristics: TableInfo = read(connection, "characteristics")
        if (!_infoCharacteristics.equals(_existingCharacteristics)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |characteristics(com.battlebarge.agnostic.data.local.CharacteristicEntity).
              | Expected:
              |""".trimMargin() + _infoCharacteristics + """
              |
              | Found:
              |""".trimMargin() + _existingCharacteristics)
        }
        val _columnsConstraints: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsConstraints.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConstraints.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConstraints.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConstraints.put("singularity_id", TableInfo.Column("singularity_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConstraints.put("type", TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConstraints.put("value", TableInfo.Column("value", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConstraints.put("field", TableInfo.Column("field", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConstraints.put("scope", TableInfo.Column("scope", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysConstraints: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesConstraints: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesConstraints.add(TableInfo.Index("index_constraints_gamesystem_id_faction_id_singularity_id", false, listOf("gamesystem_id", "faction_id", "singularity_id"), listOf("ASC", "ASC", "ASC")))
        _indicesConstraints.add(TableInfo.Index("index_constraints_singularity_id", false, listOf("singularity_id"), listOf("ASC")))
        val _infoConstraints: TableInfo = TableInfo("constraints", _columnsConstraints, _foreignKeysConstraints, _indicesConstraints)
        val _existingConstraints: TableInfo = read(connection, "constraints")
        if (!_infoConstraints.equals(_existingConstraints)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |constraints(com.battlebarge.agnostic.data.local.ConstraintEntity).
              | Expected:
              |""".trimMargin() + _infoConstraints + """
              |
              | Found:
              |""".trimMargin() + _existingConstraints)
        }
        val _columnsArgonauts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsArgonauts.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("roster_id", TableInfo.Column("roster_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("singularity_id", TableInfo.Column("singularity_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("parent_argonaut_id", TableInfo.Column("parent_argonaut_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("personal_name", TableInfo.Column("personal_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("designation", TableInfo.Column("designation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("personality", TableInfo.Column("personality", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("image_uri", TableInfo.Column("image_uri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("is_warlord", TableInfo.Column("is_warlord", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("model_count", TableInfo.Column("model_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("base_points", TableInfo.Column("base_points", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("model_cost", TableInfo.Column("model_cost", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("wargear_points", TableInfo.Column("wargear_points", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsArgonauts.put("rule_sha", TableInfo.Column("rule_sha", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysArgonauts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysArgonauts.add(TableInfo.ForeignKey("rosters", "CASCADE", "NO ACTION", listOf("roster_id"), listOf("id")))
        _foreignKeysArgonauts.add(TableInfo.ForeignKey("argonauts", "CASCADE", "NO ACTION", listOf("parent_argonaut_id"), listOf("id")))
        val _indicesArgonauts: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesArgonauts.add(TableInfo.Index("index_argonauts_singularity_id", false, listOf("singularity_id"), listOf("ASC")))
        _indicesArgonauts.add(TableInfo.Index("index_argonauts_roster_id", false, listOf("roster_id"), listOf("ASC")))
        _indicesArgonauts.add(TableInfo.Index("index_argonauts_parent_argonaut_id", false, listOf("parent_argonaut_id"), listOf("ASC")))
        _indicesArgonauts.add(TableInfo.Index("index_argonauts_gamesystem_id_faction_id", false, listOf("gamesystem_id", "faction_id"), listOf("ASC", "ASC")))
        _indicesArgonauts.add(TableInfo.Index("index_argonauts_gamesystem_id_faction_id_singularity_id", false, listOf("gamesystem_id", "faction_id", "singularity_id"), listOf("ASC", "ASC", "ASC")))
        val _infoArgonauts: TableInfo = TableInfo("argonauts", _columnsArgonauts, _foreignKeysArgonauts, _indicesArgonauts)
        val _existingArgonauts: TableInfo = read(connection, "argonauts")
        if (!_infoArgonauts.equals(_existingArgonauts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |argonauts(com.battlebarge.agnostic.data.local.ArgonautEntity).
              | Expected:
              |""".trimMargin() + _infoArgonauts + """
              |
              | Found:
              |""".trimMargin() + _existingArgonauts)
        }
        val _columnsGameSystemMetadata: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGameSystemMetadata.put("repoName", TableInfo.Column("repoName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystemMetadata.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystemMetadata.put("displayName", TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystemMetadata.put("owner", TableInfo.Column("owner", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGameSystemMetadata.put("lastUpdated", TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGameSystemMetadata: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesGameSystemMetadata: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoGameSystemMetadata: TableInfo = TableInfo("game_system_metadata", _columnsGameSystemMetadata, _foreignKeysGameSystemMetadata, _indicesGameSystemMetadata)
        val _existingGameSystemMetadata: TableInfo = read(connection, "game_system_metadata")
        if (!_infoGameSystemMetadata.equals(_existingGameSystemMetadata)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |game_system_metadata(com.battlebarge.agnostic.data.local.GameSystemMetadataEntity).
              | Expected:
              |""".trimMargin() + _infoGameSystemMetadata + """
              |
              | Found:
              |""".trimMargin() + _existingGameSystemMetadata)
        }
        val _columnsSyncMetadata: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSyncMetadata.put("file_path", TableInfo.Column("file_path", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncMetadata.put("gamesystem_id", TableInfo.Column("gamesystem_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncMetadata.put("sha", TableInfo.Column("sha", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncMetadata.put("faction_id", TableInfo.Column("faction_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncMetadata.put("last_synced", TableInfo.Column("last_synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSyncMetadata: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSyncMetadata: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSyncMetadata: TableInfo = TableInfo("sync_metadata", _columnsSyncMetadata, _foreignKeysSyncMetadata, _indicesSyncMetadata)
        val _existingSyncMetadata: TableInfo = read(connection, "sync_metadata")
        if (!_infoSyncMetadata.equals(_existingSyncMetadata)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sync_metadata(com.battlebarge.agnostic.data.local.SyncMetadataEntity).
              | Expected:
              |""".trimMargin() + _infoSyncMetadata + """
              |
              | Found:
              |""".trimMargin() + _existingSyncMetadata)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "game_systems", "factions", "unit_datasheets", "weapon_profiles", "rosters", "roster_units", "singularities", "singularity_tags", "category_links", "costs", "characteristics", "constraints", "argonauts", "game_system_metadata", "sync_metadata")
  }

  public override fun clearAllTables() {
    super.performClear(true, "game_systems", "factions", "unit_datasheets", "weapon_profiles", "rosters", "roster_units", "singularities", "singularity_tags", "category_links", "costs", "characteristics", "constraints", "argonauts", "game_system_metadata", "sync_metadata")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(EngineDao::class, EngineDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SingularityDao::class, SingularityDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun engineDao(): EngineDao = _engineDao.value

  public override fun singularityDao(): SingularityDao = _singularityDao.value
}
