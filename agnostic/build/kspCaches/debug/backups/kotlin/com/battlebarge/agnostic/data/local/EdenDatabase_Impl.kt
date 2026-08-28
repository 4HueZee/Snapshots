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
  private val _singularityDao: Lazy<SingularityDao> = lazy {
    SingularityDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "55aab619ad0267733966360e39b59137", "65dad9bce3b8560a270bff01d5a95a97") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `singularities` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `value` TEXT, `parent_id` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_type` ON `singularities` (`type`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularities_parent_id` ON `singularities` (`parent_id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `singularity_tags` (`singularity_id` TEXT NOT NULL, `tag` TEXT NOT NULL, PRIMARY KEY(`singularity_id`, `tag`), FOREIGN KEY(`singularity_id`) REFERENCES `singularities`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularity_tags_singularity_id` ON `singularity_tags` (`singularity_id`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_singularity_tags_tag` ON `singularity_tags` (`tag`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '55aab619ad0267733966360e39b59137')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `singularities`")
        connection.execSQL("DROP TABLE IF EXISTS `singularity_tags`")
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
        val _columnsSingularities: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSingularities.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("type", TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("value", TableInfo.Column("value", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularities.put("parent_id", TableInfo.Column("parent_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSingularities: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSingularities: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSingularities.add(TableInfo.Index("index_singularities_type", false, listOf("type"), listOf("ASC")))
        _indicesSingularities.add(TableInfo.Index("index_singularities_parent_id", false, listOf("parent_id"), listOf("ASC")))
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
        _columnsSingularityTags.put("singularity_id", TableInfo.Column("singularity_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSingularityTags.put("tag", TableInfo.Column("tag", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSingularityTags: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysSingularityTags.add(TableInfo.ForeignKey("singularities", "CASCADE", "NO ACTION", listOf("singularity_id"), listOf("id")))
        val _indicesSingularityTags: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSingularityTags.add(TableInfo.Index("index_singularity_tags_singularity_id", false, listOf("singularity_id"), listOf("ASC")))
        _indicesSingularityTags.add(TableInfo.Index("index_singularity_tags_tag", false, listOf("tag"), listOf("ASC")))
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
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "singularities", "singularity_tags")
  }

  public override fun clearAllTables() {
    super.performClear(true, "singularities", "singularity_tags")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
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

  public override fun singularityDao(): SingularityDao = _singularityDao.value
}
