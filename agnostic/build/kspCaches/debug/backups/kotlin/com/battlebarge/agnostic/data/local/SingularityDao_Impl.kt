package com.battlebarge.agnostic.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SingularityDao_Impl(
  __db: RoomDatabase,
) : SingularityDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfArgonautEntity: EntityInsertAdapter<ArgonautEntity>

  private val __insertAdapterOfRosterEntity: EntityInsertAdapter<RosterEntity>

  private val __insertAdapterOfGameSystemMetadataEntity:
      EntityInsertAdapter<GameSystemMetadataEntity>

  private val __insertAdapterOfSyncMetadataEntity: EntityInsertAdapter<SyncMetadataEntity>

  private val __insertAdapterOfSingularityEntity: EntityInsertAdapter<SingularityEntity>

  private val __insertAdapterOfSingularityTagEntity: EntityInsertAdapter<SingularityTagEntity>

  private val __insertAdapterOfCategoryLinkEntity: EntityInsertAdapter<CategoryLinkEntity>

  private val __insertAdapterOfCostEntity: EntityInsertAdapter<CostEntity>

  private val __insertAdapterOfCharacteristicEntity: EntityInsertAdapter<CharacteristicEntity>

  private val __insertAdapterOfConstraintEntity: EntityInsertAdapter<ConstraintEntity>

  private val __updateAdapterOfSingularityEntity: EntityDeleteOrUpdateAdapter<SingularityEntity>

  private val __updateAdapterOfSingularityTagEntity:
      EntityDeleteOrUpdateAdapter<SingularityTagEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfArgonautEntity = object : EntityInsertAdapter<ArgonautEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `argonauts` (`id`,`roster_id`,`singularity_id`,`gamesystem_id`,`faction_id`,`parent_argonaut_id`,`personal_name`,`designation`,`personality`,`image_uri`,`is_warlord`,`model_count`,`base_points`,`model_cost`,`wargear_points`,`rule_sha`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ArgonautEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.rosterId)
        statement.bindText(3, entity.singularityId)
        statement.bindText(4, entity.gamesystemId)
        statement.bindText(5, entity.factionId)
        val _tmpParentArgonautId: String? = entity.parentArgonautId
        if (_tmpParentArgonautId == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpParentArgonautId)
        }
        statement.bindText(7, entity.personalName)
        val _tmpDesignation: String? = entity.designation
        if (_tmpDesignation == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpDesignation)
        }
        val _tmpPersonality: String? = entity.personality
        if (_tmpPersonality == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpPersonality)
        }
        val _tmpImageUri: String? = entity.imageUri
        if (_tmpImageUri == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpImageUri)
        }
        val _tmp: Int = if (entity.isWarlord) 1 else 0
        statement.bindLong(11, _tmp.toLong())
        statement.bindLong(12, entity.modelCount.toLong())
        statement.bindLong(13, entity.basePoints.toLong())
        statement.bindLong(14, entity.modelCost.toLong())
        statement.bindLong(15, entity.wargearPoints.toLong())
        val _tmpRuleSha: String? = entity.ruleSha
        if (_tmpRuleSha == null) {
          statement.bindNull(16)
        } else {
          statement.bindText(16, _tmpRuleSha)
        }
      }
    }
    this.__insertAdapterOfRosterEntity = object : EntityInsertAdapter<RosterEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `rosters` (`id`,`name`,`gamesystem_id`,`faction_id`,`points_limit`,`created_at`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RosterEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.gamesystemId)
        statement.bindText(4, entity.factionId)
        statement.bindLong(5, entity.pointsLimit.toLong())
        statement.bindLong(6, entity.createdAt)
      }
    }
    this.__insertAdapterOfGameSystemMetadataEntity = object : EntityInsertAdapter<GameSystemMetadataEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `game_system_metadata` (`repoName`,`gamesystem_id`,`displayName`,`owner`,`lastUpdated`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: GameSystemMetadataEntity) {
        statement.bindText(1, entity.repoName)
        val _tmpGamesystemId: String? = entity.gamesystemId
        if (_tmpGamesystemId == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpGamesystemId)
        }
        statement.bindText(3, entity.displayName)
        statement.bindText(4, entity.owner)
        statement.bindLong(5, entity.lastUpdated)
      }
    }
    this.__insertAdapterOfSyncMetadataEntity = object : EntityInsertAdapter<SyncMetadataEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `sync_metadata` (`file_path`,`gamesystem_id`,`sha`,`faction_id`,`last_synced`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SyncMetadataEntity) {
        statement.bindText(1, entity.filePath)
        statement.bindText(2, entity.gameSystemId)
        statement.bindText(3, entity.sha)
        statement.bindText(4, entity.factionId)
        statement.bindLong(5, entity.lastSynced)
      }
    }
    this.__insertAdapterOfSingularityEntity = object : EntityInsertAdapter<SingularityEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `singularities` (`id`,`gamesystem_id`,`faction_id`,`name`,`xml_tag`,`entry_type`,`value`,`parent_id`,`target_id`,`link_type`,`category`,`gamesystem_name`,`faction_name`,`is_awakened`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SingularityEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.gamesystemId)
        statement.bindText(3, entity.factionId)
        statement.bindText(4, entity.name)
        statement.bindText(5, entity.xmlTag)
        val _tmpEntryType: String? = entity.entryType
        if (_tmpEntryType == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpEntryType)
        }
        val _tmpValue: String? = entity.value
        if (_tmpValue == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpValue)
        }
        val _tmpParentId: String? = entity.parentId
        if (_tmpParentId == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpParentId)
        }
        val _tmpTargetId: String? = entity.targetId
        if (_tmpTargetId == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpTargetId)
        }
        val _tmpLinkType: String? = entity.linkType
        if (_tmpLinkType == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpLinkType)
        }
        val _tmpCategory: String? = entity.category
        if (_tmpCategory == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpCategory)
        }
        statement.bindText(12, entity.gamesystemName)
        statement.bindText(13, entity.factionName)
        val _tmp: Int = if (entity.isAwakened) 1 else 0
        statement.bindLong(14, _tmp.toLong())
      }
    }
    this.__insertAdapterOfSingularityTagEntity = object : EntityInsertAdapter<SingularityTagEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `singularity_tags` (`gamesystem_id`,`faction_id`,`singularity_id`,`tag`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SingularityTagEntity) {
        statement.bindText(1, entity.gamesystemId)
        statement.bindText(2, entity.factionId)
        statement.bindText(3, entity.singularityId)
        statement.bindText(4, entity.tag)
      }
    }
    this.__insertAdapterOfCategoryLinkEntity = object : EntityInsertAdapter<CategoryLinkEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `category_links` (`gamesystem_id`,`faction_id`,`singularity_id`,`target_id`,`category_name`,`is_primary`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CategoryLinkEntity) {
        statement.bindText(1, entity.gamesystemId)
        statement.bindText(2, entity.factionId)
        statement.bindText(3, entity.singularityId)
        statement.bindText(4, entity.targetId)
        statement.bindText(5, entity.categoryName)
        val _tmp: Int = if (entity.isPrimary) 1 else 0
        statement.bindLong(6, _tmp.toLong())
      }
    }
    this.__insertAdapterOfCostEntity = object : EntityInsertAdapter<CostEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `costs` (`gamesystem_id`,`faction_id`,`singularity_id`,`name`,`value`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CostEntity) {
        statement.bindText(1, entity.gamesystemId)
        statement.bindText(2, entity.factionId)
        statement.bindText(3, entity.singularityId)
        statement.bindText(4, entity.name)
        statement.bindDouble(5, entity.value)
      }
    }
    this.__insertAdapterOfCharacteristicEntity = object : EntityInsertAdapter<CharacteristicEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `characteristics` (`gamesystem_id`,`faction_id`,`singularity_id`,`profile_name`,`profile_type`,`stat_name`,`stat_value`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CharacteristicEntity) {
        statement.bindText(1, entity.gamesystemId)
        statement.bindText(2, entity.factionId)
        statement.bindText(3, entity.singularityId)
        statement.bindText(4, entity.profileName)
        statement.bindText(5, entity.profileType)
        statement.bindText(6, entity.statName)
        statement.bindText(7, entity.statValue)
      }
    }
    this.__insertAdapterOfConstraintEntity = object : EntityInsertAdapter<ConstraintEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `constraints` (`id`,`gamesystem_id`,`faction_id`,`singularity_id`,`type`,`value`,`field`,`scope`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ConstraintEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.gamesystemId)
        statement.bindText(3, entity.factionId)
        statement.bindText(4, entity.singularityId)
        statement.bindText(5, entity.type)
        statement.bindLong(6, entity.value.toLong())
        val _tmpField: String? = entity.field
        if (_tmpField == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpField)
        }
        val _tmpScope: String? = entity.scope
        if (_tmpScope == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpScope)
        }
      }
    }
    this.__updateAdapterOfSingularityEntity = object : EntityDeleteOrUpdateAdapter<SingularityEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `singularities` SET `id` = ?,`gamesystem_id` = ?,`faction_id` = ?,`name` = ?,`xml_tag` = ?,`entry_type` = ?,`value` = ?,`parent_id` = ?,`target_id` = ?,`link_type` = ?,`category` = ?,`gamesystem_name` = ?,`faction_name` = ?,`is_awakened` = ? WHERE `gamesystem_id` = ? AND `faction_id` = ? AND `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SingularityEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.gamesystemId)
        statement.bindText(3, entity.factionId)
        statement.bindText(4, entity.name)
        statement.bindText(5, entity.xmlTag)
        val _tmpEntryType: String? = entity.entryType
        if (_tmpEntryType == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpEntryType)
        }
        val _tmpValue: String? = entity.value
        if (_tmpValue == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpValue)
        }
        val _tmpParentId: String? = entity.parentId
        if (_tmpParentId == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpParentId)
        }
        val _tmpTargetId: String? = entity.targetId
        if (_tmpTargetId == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpTargetId)
        }
        val _tmpLinkType: String? = entity.linkType
        if (_tmpLinkType == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpLinkType)
        }
        val _tmpCategory: String? = entity.category
        if (_tmpCategory == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpCategory)
        }
        statement.bindText(12, entity.gamesystemName)
        statement.bindText(13, entity.factionName)
        val _tmp: Int = if (entity.isAwakened) 1 else 0
        statement.bindLong(14, _tmp.toLong())
        statement.bindText(15, entity.gamesystemId)
        statement.bindText(16, entity.factionId)
        statement.bindText(17, entity.id)
      }
    }
    this.__updateAdapterOfSingularityTagEntity = object : EntityDeleteOrUpdateAdapter<SingularityTagEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `singularity_tags` SET `gamesystem_id` = ?,`faction_id` = ?,`singularity_id` = ?,`tag` = ? WHERE `gamesystem_id` = ? AND `faction_id` = ? AND `singularity_id` = ? AND `tag` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SingularityTagEntity) {
        statement.bindText(1, entity.gamesystemId)
        statement.bindText(2, entity.factionId)
        statement.bindText(3, entity.singularityId)
        statement.bindText(4, entity.tag)
        statement.bindText(5, entity.gamesystemId)
        statement.bindText(6, entity.factionId)
        statement.bindText(7, entity.singularityId)
        statement.bindText(8, entity.tag)
      }
    }
  }

  public override suspend fun insertArgonaut(argonaut: ArgonautEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfArgonautEntity.insert(_connection, argonaut)
  }

  public override suspend fun insertRoster(roster: RosterEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfRosterEntity.insert(_connection, roster)
  }

  public override suspend fun insertMetadata(metadata: GameSystemMetadataEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfGameSystemMetadataEntity.insert(_connection, metadata)
  }

  public override suspend fun insertSyncMetadata(metadata: SyncMetadataEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSyncMetadataEntity.insert(_connection, metadata)
  }

  public override suspend fun insertSingularitiesIgnore(singularities: List<SingularityEntity>): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfSingularityEntity.insertAndReturnIdsList(_connection, singularities)
    _result
  }

  public override suspend fun insertTagsIgnore(tags: List<SingularityTagEntity>): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfSingularityTagEntity.insertAndReturnIdsList(_connection, tags)
    _result
  }

  public override suspend fun insertCategoryLinksIgnore(links: List<CategoryLinkEntity>): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfCategoryLinkEntity.insertAndReturnIdsList(_connection, links)
    _result
  }

  public override suspend fun insertCostsIgnore(costs: List<CostEntity>): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfCostEntity.insertAndReturnIdsList(_connection, costs)
    _result
  }

  public override suspend fun insertCharacteristicsIgnore(characteristics: List<CharacteristicEntity>): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfCharacteristicEntity.insertAndReturnIdsList(_connection, characteristics)
    _result
  }

  public override suspend fun insertConstraintsIgnore(constraints: List<ConstraintEntity>): List<Long> = performSuspending(__db, false, true) { _connection ->
    val _result: List<Long> = __insertAdapterOfConstraintEntity.insertAndReturnIdsList(_connection, constraints)
    _result
  }

  public override suspend fun updateSingularities(singularities: List<SingularityEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfSingularityEntity.handleMultiple(_connection, singularities)
  }

  public override suspend fun updateTags(tags: List<SingularityTagEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfSingularityTagEntity.handleMultiple(_connection, tags)
  }

  public override suspend fun clearAllRules(): Unit = performInTransactionSuspending(__db) {
    super@SingularityDao_Impl.clearAllRules()
  }

  public override suspend fun clearUserDatabase(): Unit = performInTransactionSuspending(__db) {
    super@SingularityDao_Impl.clearUserDatabase()
  }

  public override suspend fun clearGameSystemData(gsId: String): Unit = performInTransactionSuspending(__db) {
    super@SingularityDao_Impl.clearGameSystemData(gsId)
  }

  public override suspend fun upsertFactionData(
    gamesystemId: String,
    factionId: String,
    singularities: List<SingularityEntity>,
    tags: List<SingularityTagEntity>,
  ): Unit = performInTransactionSuspending(__db) {
    super@SingularityDao_Impl.upsertFactionData(gamesystemId, factionId, singularities, tags)
  }

  public override suspend fun getSingularityById(
    gsId: String,
    fId: String,
    id: String,
  ): SingularityEntity? {
    val _sql: String = "SELECT * FROM singularities WHERE gamesystem_id = ? AND faction_id = ? AND id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: SingularityEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _result = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSingularityByTargetId(gsId: String, id: String): SingularityEntity? {
    val _sql: String = "SELECT * FROM singularities WHERE gamesystem_id = ? AND id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: SingularityEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _result = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getChildrenOf(
    gsId: String,
    fId: String,
    parentId: String,
  ): Flow<List<SingularityEntity>> {
    val _sql: String = "SELECT * FROM singularities WHERE gamesystem_id = ? AND faction_id = ? AND parent_id = ?"
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, parentId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getChildrenList(
    gsId: String,
    fId: String,
    parentId: String,
  ): List<SingularityEntity> {
    val _sql: String = "SELECT * FROM singularities WHERE gamesystem_id = ? AND faction_id = ? AND parent_id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, parentId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRootNodes(gsId: String, fId: String): Flow<List<SingularityEntity>> {
    val _sql: String = "SELECT * FROM singularities WHERE gamesystem_id = ? AND faction_id = ? AND parent_id IS NULL"
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTagsForSingularity(
    gsId: String,
    fId: String,
    sId: String,
  ): List<String> {
    val _sql: String = "SELECT tag FROM singularity_tags WHERE gamesystem_id = ? AND faction_id = ? AND singularity_id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, sId)
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getArgonautById(id: String): ArgonautEntity? {
    val _sql: String = "SELECT * FROM argonauts WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfRosterId: Int = getColumnIndexOrThrow(_stmt, "roster_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfParentArgonautId: Int = getColumnIndexOrThrow(_stmt, "parent_argonaut_id")
        val _columnIndexOfPersonalName: Int = getColumnIndexOrThrow(_stmt, "personal_name")
        val _columnIndexOfDesignation: Int = getColumnIndexOrThrow(_stmt, "designation")
        val _columnIndexOfPersonality: Int = getColumnIndexOrThrow(_stmt, "personality")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "image_uri")
        val _columnIndexOfIsWarlord: Int = getColumnIndexOrThrow(_stmt, "is_warlord")
        val _columnIndexOfModelCount: Int = getColumnIndexOrThrow(_stmt, "model_count")
        val _columnIndexOfBasePoints: Int = getColumnIndexOrThrow(_stmt, "base_points")
        val _columnIndexOfModelCost: Int = getColumnIndexOrThrow(_stmt, "model_cost")
        val _columnIndexOfWargearPoints: Int = getColumnIndexOrThrow(_stmt, "wargear_points")
        val _columnIndexOfRuleSha: Int = getColumnIndexOrThrow(_stmt, "rule_sha")
        val _result: ArgonautEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpRosterId: String
          _tmpRosterId = _stmt.getText(_columnIndexOfRosterId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpParentArgonautId: String?
          if (_stmt.isNull(_columnIndexOfParentArgonautId)) {
            _tmpParentArgonautId = null
          } else {
            _tmpParentArgonautId = _stmt.getText(_columnIndexOfParentArgonautId)
          }
          val _tmpPersonalName: String
          _tmpPersonalName = _stmt.getText(_columnIndexOfPersonalName)
          val _tmpDesignation: String?
          if (_stmt.isNull(_columnIndexOfDesignation)) {
            _tmpDesignation = null
          } else {
            _tmpDesignation = _stmt.getText(_columnIndexOfDesignation)
          }
          val _tmpPersonality: String?
          if (_stmt.isNull(_columnIndexOfPersonality)) {
            _tmpPersonality = null
          } else {
            _tmpPersonality = _stmt.getText(_columnIndexOfPersonality)
          }
          val _tmpImageUri: String?
          if (_stmt.isNull(_columnIndexOfImageUri)) {
            _tmpImageUri = null
          } else {
            _tmpImageUri = _stmt.getText(_columnIndexOfImageUri)
          }
          val _tmpIsWarlord: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsWarlord).toInt()
          _tmpIsWarlord = _tmp != 0
          val _tmpModelCount: Int
          _tmpModelCount = _stmt.getLong(_columnIndexOfModelCount).toInt()
          val _tmpBasePoints: Int
          _tmpBasePoints = _stmt.getLong(_columnIndexOfBasePoints).toInt()
          val _tmpModelCost: Int
          _tmpModelCost = _stmt.getLong(_columnIndexOfModelCost).toInt()
          val _tmpWargearPoints: Int
          _tmpWargearPoints = _stmt.getLong(_columnIndexOfWargearPoints).toInt()
          val _tmpRuleSha: String?
          if (_stmt.isNull(_columnIndexOfRuleSha)) {
            _tmpRuleSha = null
          } else {
            _tmpRuleSha = _stmt.getText(_columnIndexOfRuleSha)
          }
          _result = ArgonautEntity(_tmpId,_tmpRosterId,_tmpSingularityId,_tmpGamesystemId,_tmpFactionId,_tmpParentArgonautId,_tmpPersonalName,_tmpDesignation,_tmpPersonality,_tmpImageUri,_tmpIsWarlord,_tmpModelCount,_tmpBasePoints,_tmpModelCost,_tmpWargearPoints,_tmpRuleSha)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllArgonauts(): Flow<List<ArgonautEntity>> {
    val _sql: String = "SELECT * FROM argonauts"
    return createFlow(__db, false, arrayOf("argonauts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfRosterId: Int = getColumnIndexOrThrow(_stmt, "roster_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfParentArgonautId: Int = getColumnIndexOrThrow(_stmt, "parent_argonaut_id")
        val _columnIndexOfPersonalName: Int = getColumnIndexOrThrow(_stmt, "personal_name")
        val _columnIndexOfDesignation: Int = getColumnIndexOrThrow(_stmt, "designation")
        val _columnIndexOfPersonality: Int = getColumnIndexOrThrow(_stmt, "personality")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "image_uri")
        val _columnIndexOfIsWarlord: Int = getColumnIndexOrThrow(_stmt, "is_warlord")
        val _columnIndexOfModelCount: Int = getColumnIndexOrThrow(_stmt, "model_count")
        val _columnIndexOfBasePoints: Int = getColumnIndexOrThrow(_stmt, "base_points")
        val _columnIndexOfModelCost: Int = getColumnIndexOrThrow(_stmt, "model_cost")
        val _columnIndexOfWargearPoints: Int = getColumnIndexOrThrow(_stmt, "wargear_points")
        val _columnIndexOfRuleSha: Int = getColumnIndexOrThrow(_stmt, "rule_sha")
        val _result: MutableList<ArgonautEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ArgonautEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpRosterId: String
          _tmpRosterId = _stmt.getText(_columnIndexOfRosterId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpParentArgonautId: String?
          if (_stmt.isNull(_columnIndexOfParentArgonautId)) {
            _tmpParentArgonautId = null
          } else {
            _tmpParentArgonautId = _stmt.getText(_columnIndexOfParentArgonautId)
          }
          val _tmpPersonalName: String
          _tmpPersonalName = _stmt.getText(_columnIndexOfPersonalName)
          val _tmpDesignation: String?
          if (_stmt.isNull(_columnIndexOfDesignation)) {
            _tmpDesignation = null
          } else {
            _tmpDesignation = _stmt.getText(_columnIndexOfDesignation)
          }
          val _tmpPersonality: String?
          if (_stmt.isNull(_columnIndexOfPersonality)) {
            _tmpPersonality = null
          } else {
            _tmpPersonality = _stmt.getText(_columnIndexOfPersonality)
          }
          val _tmpImageUri: String?
          if (_stmt.isNull(_columnIndexOfImageUri)) {
            _tmpImageUri = null
          } else {
            _tmpImageUri = _stmt.getText(_columnIndexOfImageUri)
          }
          val _tmpIsWarlord: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsWarlord).toInt()
          _tmpIsWarlord = _tmp != 0
          val _tmpModelCount: Int
          _tmpModelCount = _stmt.getLong(_columnIndexOfModelCount).toInt()
          val _tmpBasePoints: Int
          _tmpBasePoints = _stmt.getLong(_columnIndexOfBasePoints).toInt()
          val _tmpModelCost: Int
          _tmpModelCost = _stmt.getLong(_columnIndexOfModelCost).toInt()
          val _tmpWargearPoints: Int
          _tmpWargearPoints = _stmt.getLong(_columnIndexOfWargearPoints).toInt()
          val _tmpRuleSha: String?
          if (_stmt.isNull(_columnIndexOfRuleSha)) {
            _tmpRuleSha = null
          } else {
            _tmpRuleSha = _stmt.getText(_columnIndexOfRuleSha)
          }
          _item = ArgonautEntity(_tmpId,_tmpRosterId,_tmpSingularityId,_tmpGamesystemId,_tmpFactionId,_tmpParentArgonautId,_tmpPersonalName,_tmpDesignation,_tmpPersonality,_tmpImageUri,_tmpIsWarlord,_tmpModelCount,_tmpBasePoints,_tmpModelCost,_tmpWargearPoints,_tmpRuleSha)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getArgonautsForRoster(rosterId: String): Flow<List<ArgonautEntity>> {
    val _sql: String = "SELECT * FROM argonauts WHERE roster_id = ?"
    return createFlow(__db, false, arrayOf("argonauts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, rosterId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfRosterId: Int = getColumnIndexOrThrow(_stmt, "roster_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfParentArgonautId: Int = getColumnIndexOrThrow(_stmt, "parent_argonaut_id")
        val _columnIndexOfPersonalName: Int = getColumnIndexOrThrow(_stmt, "personal_name")
        val _columnIndexOfDesignation: Int = getColumnIndexOrThrow(_stmt, "designation")
        val _columnIndexOfPersonality: Int = getColumnIndexOrThrow(_stmt, "personality")
        val _columnIndexOfImageUri: Int = getColumnIndexOrThrow(_stmt, "image_uri")
        val _columnIndexOfIsWarlord: Int = getColumnIndexOrThrow(_stmt, "is_warlord")
        val _columnIndexOfModelCount: Int = getColumnIndexOrThrow(_stmt, "model_count")
        val _columnIndexOfBasePoints: Int = getColumnIndexOrThrow(_stmt, "base_points")
        val _columnIndexOfModelCost: Int = getColumnIndexOrThrow(_stmt, "model_cost")
        val _columnIndexOfWargearPoints: Int = getColumnIndexOrThrow(_stmt, "wargear_points")
        val _columnIndexOfRuleSha: Int = getColumnIndexOrThrow(_stmt, "rule_sha")
        val _result: MutableList<ArgonautEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ArgonautEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpRosterId: String
          _tmpRosterId = _stmt.getText(_columnIndexOfRosterId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpParentArgonautId: String?
          if (_stmt.isNull(_columnIndexOfParentArgonautId)) {
            _tmpParentArgonautId = null
          } else {
            _tmpParentArgonautId = _stmt.getText(_columnIndexOfParentArgonautId)
          }
          val _tmpPersonalName: String
          _tmpPersonalName = _stmt.getText(_columnIndexOfPersonalName)
          val _tmpDesignation: String?
          if (_stmt.isNull(_columnIndexOfDesignation)) {
            _tmpDesignation = null
          } else {
            _tmpDesignation = _stmt.getText(_columnIndexOfDesignation)
          }
          val _tmpPersonality: String?
          if (_stmt.isNull(_columnIndexOfPersonality)) {
            _tmpPersonality = null
          } else {
            _tmpPersonality = _stmt.getText(_columnIndexOfPersonality)
          }
          val _tmpImageUri: String?
          if (_stmt.isNull(_columnIndexOfImageUri)) {
            _tmpImageUri = null
          } else {
            _tmpImageUri = _stmt.getText(_columnIndexOfImageUri)
          }
          val _tmpIsWarlord: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsWarlord).toInt()
          _tmpIsWarlord = _tmp != 0
          val _tmpModelCount: Int
          _tmpModelCount = _stmt.getLong(_columnIndexOfModelCount).toInt()
          val _tmpBasePoints: Int
          _tmpBasePoints = _stmt.getLong(_columnIndexOfBasePoints).toInt()
          val _tmpModelCost: Int
          _tmpModelCost = _stmt.getLong(_columnIndexOfModelCost).toInt()
          val _tmpWargearPoints: Int
          _tmpWargearPoints = _stmt.getLong(_columnIndexOfWargearPoints).toInt()
          val _tmpRuleSha: String?
          if (_stmt.isNull(_columnIndexOfRuleSha)) {
            _tmpRuleSha = null
          } else {
            _tmpRuleSha = _stmt.getText(_columnIndexOfRuleSha)
          }
          _item = ArgonautEntity(_tmpId,_tmpRosterId,_tmpSingularityId,_tmpGamesystemId,_tmpFactionId,_tmpParentArgonautId,_tmpPersonalName,_tmpDesignation,_tmpPersonality,_tmpImageUri,_tmpIsWarlord,_tmpModelCount,_tmpBasePoints,_tmpModelCost,_tmpWargearPoints,_tmpRuleSha)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllRosters(): Flow<List<RosterEntity>> {
    val _sql: String = "SELECT * FROM rosters"
    return createFlow(__db, false, arrayOf("rosters")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfPointsLimit: Int = getColumnIndexOrThrow(_stmt, "points_limit")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _result: MutableList<RosterEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: RosterEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpPointsLimit: Int
          _tmpPointsLimit = _stmt.getLong(_columnIndexOfPointsLimit).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = RosterEntity(_tmpId,_tmpName,_tmpGamesystemId,_tmpFactionId,_tmpPointsLimit,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getRosterById(id: String): RosterEntity? {
    val _sql: String = "SELECT * FROM rosters WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfPointsLimit: Int = getColumnIndexOrThrow(_stmt, "points_limit")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "created_at")
        val _result: RosterEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpPointsLimit: Int
          _tmpPointsLimit = _stmt.getLong(_columnIndexOfPointsLimit).toInt()
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = RosterEntity(_tmpId,_tmpName,_tmpGamesystemId,_tmpFactionId,_tmpPointsLimit,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDownloadedFactionIds(): Flow<List<String>> {
    val _sql: String = "SELECT DISTINCT faction_id FROM singularities"
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDownloadedGameSystems(): Flow<List<DownloadedGameSystem>> {
    val _sql: String = """
        |
        |        SELECT DISTINCT 
        |            s.gamesystem_id AS gamesystem_id, 
        |            IFNULL(m.displayName, s.gamesystem_name) AS gamesystem_name, 
        |            IFNULL(m.lastUpdated, 1725300000000) AS last_updated 
        |        FROM singularities s 
        |        LEFT JOIN game_system_metadata m ON s.gamesystem_id = m.repoName
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("singularities", "game_system_metadata")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = 0
        val _columnIndexOfName: Int = 1
        val _columnIndexOfLastUpdated: Int = 2
        val _result: MutableList<DownloadedGameSystem> = mutableListOf()
        while (_stmt.step()) {
          val _item: DownloadedGameSystem
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLastUpdated: Long
          _tmpLastUpdated = _stmt.getLong(_columnIndexOfLastUpdated)
          _item = DownloadedGameSystem(_tmpId,_tmpName,_tmpLastUpdated)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDownloadedFactions(): Flow<List<DownloadedFaction>> {
    val _sql: String = "SELECT DISTINCT faction_id, faction_name, gamesystem_id, gamesystem_name FROM singularities"
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfFactionId: Int = 0
        val _columnIndexOfFactionName: Int = 1
        val _columnIndexOfGamesystemId: Int = 2
        val _columnIndexOfGamesystemName: Int = 3
        val _result: MutableList<DownloadedFaction> = mutableListOf()
        while (_stmt.step()) {
          val _item: DownloadedFaction
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          _item = DownloadedFaction(_tmpFactionId,_tmpFactionName,_tmpGamesystemId,_tmpGamesystemName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDownloadedCataloguesDirect(gsId: String): List<DownloadedFaction> {
    val _sql: String = """
        |
        |        SELECT DISTINCT faction_id, faction_name, gamesystem_id, gamesystem_name 
        |        FROM singularities 
        |        WHERE (gamesystem_id = ? OR gamesystem_id = 'wh40k' OR gamesystem_id = 'wh40k-10th')
        |        AND xml_tag = 'catalogue'
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        val _columnIndexOfFactionId: Int = 0
        val _columnIndexOfFactionName: Int = 1
        val _columnIndexOfGamesystemId: Int = 2
        val _columnIndexOfGamesystemName: Int = 3
        val _result: MutableList<DownloadedFaction> = mutableListOf()
        while (_stmt.step()) {
          val _item: DownloadedFaction
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          _item = DownloadedFaction(_tmpFactionId,_tmpFactionName,_tmpGamesystemId,_tmpGamesystemName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDownloadedCataloguesForSystem(gsId: String): Flow<List<DownloadedFaction>> {
    val _sql: String = """
        |
        |        SELECT DISTINCT faction_id, faction_name, gamesystem_id, gamesystem_name 
        |        FROM singularities 
        |        WHERE gamesystem_id = ? 
        |        AND xml_tag = 'catalogue'
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        val _columnIndexOfFactionId: Int = 0
        val _columnIndexOfFactionName: Int = 1
        val _columnIndexOfGamesystemId: Int = 2
        val _columnIndexOfGamesystemName: Int = 3
        val _result: MutableList<DownloadedFaction> = mutableListOf()
        while (_stmt.step()) {
          val _item: DownloadedFaction
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          _item = DownloadedFaction(_tmpFactionId,_tmpFactionName,_tmpGamesystemId,_tmpGamesystemName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSingularitiesByTag(
    gsId: String,
    fId: String,
    tag: String,
  ): Flow<List<SingularityEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM singularities 
        |        WHERE gamesystem_id = ? AND faction_id = ? 
        |        AND (xml_tag LIKE '%' || ? || '%' OR xml_tag = ?)
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, tag)
        _argIndex = 4
        _stmt.bindText(_argIndex, tag)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSingularitiesByTagForSystem(gsId: String, tag: String): Flow<List<SingularityEntity>> {
    val _sql: String = """
        |
        |        SELECT DISTINCT * FROM singularities 
        |        WHERE gamesystem_id = ? 
        |        AND (xml_tag LIKE '%' || ? || '%' OR xml_tag = ?)
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, tag)
        _argIndex = 3
        _stmt.bindText(_argIndex, tag)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRootUnitsForSystem(gsId: String): Flow<List<SingularityEntity>> {
    val _sql: String = """
        |
        |        SELECT DISTINCT * FROM singularities 
        |        WHERE (gamesystem_id = ? OR gamesystem_id LIKE '%' || ? || '%' OR ? LIKE '%' || gamesystem_id || '%')
        |        AND (xml_tag = 'selectionEntry' OR xml_tag = 'entryLink' OR xml_tag LIKE '%selection%')
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 3
        _stmt.bindText(_argIndex, gsId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTopLevelEntriesForFaction(gsId: String, fId: String): Flow<List<SingularityEntity>> {
    val _sql: String = """
        |
        |        SELECT DISTINCT * FROM singularities 
        |        WHERE (gamesystem_id = ? OR gamesystem_id LIKE '%' || ? || '%' OR ? LIKE '%' || gamesystem_id || '%')
        |        AND (
        |            ? = 'ALL' 
        |            OR faction_id = ? 
        |            OR faction_id LIKE '%' || ? || '%' 
        |            OR ? LIKE '%' || faction_id || '%'
        |            OR faction_name LIKE '%' || ? || '%'
        |            OR ? LIKE '%' || faction_name || '%'
        |            OR faction_id = 'CORE'
        |        )
        |        AND (xml_tag = 'selectionEntry' OR xml_tag = 'entryLink' OR xml_tag LIKE '%selection%')
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 3
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 4
        _stmt.bindText(_argIndex, fId)
        _argIndex = 5
        _stmt.bindText(_argIndex, fId)
        _argIndex = 6
        _stmt.bindText(_argIndex, fId)
        _argIndex = 7
        _stmt.bindText(_argIndex, fId)
        _argIndex = 8
        _stmt.bindText(_argIndex, fId)
        _argIndex = 9
        _stmt.bindText(_argIndex, fId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRootUnitsForFaction(gsId: String, fId: String): Flow<List<SingularityEntity>> {
    val _sql: String = """
        |
        |        SELECT DISTINCT * FROM singularities 
        |        WHERE (gamesystem_id = ? OR gamesystem_id LIKE '%' || ? || '%' OR ? LIKE '%' || gamesystem_id || '%')
        |        AND (
        |            ? = 'ALL' 
        |            OR faction_id = ? 
        |            OR faction_id LIKE '%' || ? || '%' 
        |            OR ? LIKE '%' || faction_id || '%'
        |            OR faction_name LIKE '%' || ? || '%'
        |            OR ? LIKE '%' || faction_name || '%'
        |            OR faction_id = 'CORE'
        |        )
        |        AND (xml_tag = 'selectionEntry' OR xml_tag = 'entryLink' OR xml_tag LIKE '%selection%')
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 3
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 4
        _stmt.bindText(_argIndex, fId)
        _argIndex = 5
        _stmt.bindText(_argIndex, fId)
        _argIndex = 6
        _stmt.bindText(_argIndex, fId)
        _argIndex = 7
        _stmt.bindText(_argIndex, fId)
        _argIndex = 8
        _stmt.bindText(_argIndex, fId)
        _argIndex = 9
        _stmt.bindText(_argIndex, fId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMetadata(repoName: String): GameSystemMetadataEntity? {
    val _sql: String = "SELECT * FROM game_system_metadata WHERE repoName = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, repoName)
        val _columnIndexOfRepoName: Int = getColumnIndexOrThrow(_stmt, "repoName")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfDisplayName: Int = getColumnIndexOrThrow(_stmt, "displayName")
        val _columnIndexOfOwner: Int = getColumnIndexOrThrow(_stmt, "owner")
        val _columnIndexOfLastUpdated: Int = getColumnIndexOrThrow(_stmt, "lastUpdated")
        val _result: GameSystemMetadataEntity?
        if (_stmt.step()) {
          val _tmpRepoName: String
          _tmpRepoName = _stmt.getText(_columnIndexOfRepoName)
          val _tmpGamesystemId: String?
          if (_stmt.isNull(_columnIndexOfGamesystemId)) {
            _tmpGamesystemId = null
          } else {
            _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          }
          val _tmpDisplayName: String
          _tmpDisplayName = _stmt.getText(_columnIndexOfDisplayName)
          val _tmpOwner: String
          _tmpOwner = _stmt.getText(_columnIndexOfOwner)
          val _tmpLastUpdated: Long
          _tmpLastUpdated = _stmt.getLong(_columnIndexOfLastUpdated)
          _result = GameSystemMetadataEntity(_tmpRepoName,_tmpGamesystemId,_tmpDisplayName,_tmpOwner,_tmpLastUpdated)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSyncMetadataForSystem(gsId: String): List<SyncMetadataEntity> {
    val _sql: String = "SELECT * FROM sync_metadata WHERE gamesystem_id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "file_path")
        val _columnIndexOfGameSystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfSha: Int = getColumnIndexOrThrow(_stmt, "sha")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfLastSynced: Int = getColumnIndexOrThrow(_stmt, "last_synced")
        val _result: MutableList<SyncMetadataEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SyncMetadataEntity
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpGameSystemId: String
          _tmpGameSystemId = _stmt.getText(_columnIndexOfGameSystemId)
          val _tmpSha: String
          _tmpSha = _stmt.getText(_columnIndexOfSha)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpLastSynced: Long
          _tmpLastSynced = _stmt.getLong(_columnIndexOfLastSynced)
          _item = SyncMetadataEntity(_tmpFilePath,_tmpGameSystemId,_tmpSha,_tmpFactionId,_tmpLastSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSyncMetadataByPath(gsId: String, path: String): SyncMetadataEntity? {
    val _sql: String = "SELECT * FROM sync_metadata WHERE gamesystem_id = ? AND file_path = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, path)
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "file_path")
        val _columnIndexOfGameSystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfSha: Int = getColumnIndexOrThrow(_stmt, "sha")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfLastSynced: Int = getColumnIndexOrThrow(_stmt, "last_synced")
        val _result: SyncMetadataEntity?
        if (_stmt.step()) {
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpGameSystemId: String
          _tmpGameSystemId = _stmt.getText(_columnIndexOfGameSystemId)
          val _tmpSha: String
          _tmpSha = _stmt.getText(_columnIndexOfSha)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpLastSynced: Long
          _tmpLastSynced = _stmt.getLong(_columnIndexOfLastSynced)
          _result = SyncMetadataEntity(_tmpFilePath,_tmpGameSystemId,_tmpSha,_tmpFactionId,_tmpLastSynced)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSyncMetadataByFaction(gsId: String, fId: String): SyncMetadataEntity? {
    val _sql: String = "SELECT * FROM sync_metadata WHERE gamesystem_id = ? AND faction_id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "file_path")
        val _columnIndexOfGameSystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfSha: Int = getColumnIndexOrThrow(_stmt, "sha")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfLastSynced: Int = getColumnIndexOrThrow(_stmt, "last_synced")
        val _result: SyncMetadataEntity?
        if (_stmt.step()) {
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpGameSystemId: String
          _tmpGameSystemId = _stmt.getText(_columnIndexOfGameSystemId)
          val _tmpSha: String
          _tmpSha = _stmt.getText(_columnIndexOfSha)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpLastSynced: Long
          _tmpLastSynced = _stmt.getLong(_columnIndexOfLastSynced)
          _result = SyncMetadataEntity(_tmpFilePath,_tmpGameSystemId,_tmpSha,_tmpFactionId,_tmpLastSynced)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getConstraintsForSingularity(
    gsId: String,
    fId: String,
    singularityId: String,
  ): Flow<List<ConstraintEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM constraints
        |        WHERE gamesystem_id = ? AND faction_id = ? AND singularity_id = ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("constraints")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, singularityId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfField: Int = getColumnIndexOrThrow(_stmt, "field")
        val _columnIndexOfScope: Int = getColumnIndexOrThrow(_stmt, "scope")
        val _result: MutableList<ConstraintEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ConstraintEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpValue: Int
          _tmpValue = _stmt.getLong(_columnIndexOfValue).toInt()
          val _tmpField: String?
          if (_stmt.isNull(_columnIndexOfField)) {
            _tmpField = null
          } else {
            _tmpField = _stmt.getText(_columnIndexOfField)
          }
          val _tmpScope: String?
          if (_stmt.isNull(_columnIndexOfScope)) {
            _tmpScope = null
          } else {
            _tmpScope = _stmt.getText(_columnIndexOfScope)
          }
          _item = ConstraintEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpSingularityId,_tmpType,_tmpValue,_tmpField,_tmpScope)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getCharacteristicsForSingularity(
    gsId: String,
    fId: String,
    singularityId: String,
  ): Flow<List<CharacteristicEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM characteristics
        |        WHERE gamesystem_id = ? AND faction_id = ? AND singularity_id = ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("characteristics")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, singularityId)
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfProfileName: Int = getColumnIndexOrThrow(_stmt, "profile_name")
        val _columnIndexOfProfileType: Int = getColumnIndexOrThrow(_stmt, "profile_type")
        val _columnIndexOfStatName: Int = getColumnIndexOrThrow(_stmt, "stat_name")
        val _columnIndexOfStatValue: Int = getColumnIndexOrThrow(_stmt, "stat_value")
        val _result: MutableList<CharacteristicEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CharacteristicEntity
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpProfileName: String
          _tmpProfileName = _stmt.getText(_columnIndexOfProfileName)
          val _tmpProfileType: String
          _tmpProfileType = _stmt.getText(_columnIndexOfProfileType)
          val _tmpStatName: String
          _tmpStatName = _stmt.getText(_columnIndexOfStatName)
          val _tmpStatValue: String
          _tmpStatValue = _stmt.getText(_columnIndexOfStatValue)
          _item = CharacteristicEntity(_tmpGamesystemId,_tmpFactionId,_tmpSingularityId,_tmpProfileName,_tmpProfileType,_tmpStatName,_tmpStatValue)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCharacteristicsList(
    gsId: String,
    fId: String,
    singularityId: String,
  ): List<CharacteristicEntity> {
    val _sql: String = """
        |
        |        SELECT * FROM characteristics
        |        WHERE gamesystem_id = ? AND faction_id = ? AND singularity_id = ?
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, singularityId)
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfProfileName: Int = getColumnIndexOrThrow(_stmt, "profile_name")
        val _columnIndexOfProfileType: Int = getColumnIndexOrThrow(_stmt, "profile_type")
        val _columnIndexOfStatName: Int = getColumnIndexOrThrow(_stmt, "stat_name")
        val _columnIndexOfStatValue: Int = getColumnIndexOrThrow(_stmt, "stat_value")
        val _result: MutableList<CharacteristicEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CharacteristicEntity
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpProfileName: String
          _tmpProfileName = _stmt.getText(_columnIndexOfProfileName)
          val _tmpProfileType: String
          _tmpProfileType = _stmt.getText(_columnIndexOfProfileType)
          val _tmpStatName: String
          _tmpStatName = _stmt.getText(_columnIndexOfStatName)
          val _tmpStatValue: String
          _tmpStatValue = _stmt.getText(_columnIndexOfStatValue)
          _item = CharacteristicEntity(_tmpGamesystemId,_tmpFactionId,_tmpSingularityId,_tmpProfileName,_tmpProfileType,_tmpStatName,_tmpStatValue)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSingularityByIdFlow(gsId: String, id: String): Flow<SingularityEntity?> {
    val _sql: String = "SELECT * FROM singularities WHERE gamesystem_id = ? AND id = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: SingularityEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _result = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getUnitProfilesForUnitAndModels(
    gsId: String,
    fId: String,
    unitId: String,
  ): Flow<List<CharacteristicEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM characteristics
        |        WHERE gamesystem_id = ? AND faction_id = ? 
        |        AND (singularity_id = ? OR singularity_id IN (SELECT id FROM singularities WHERE parent_id = ?))
        |        AND (profile_type LIKE '%Unit%' OR profile_type LIKE '%Model%')
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("characteristics", "singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, unitId)
        _argIndex = 4
        _stmt.bindText(_argIndex, unitId)
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfProfileName: Int = getColumnIndexOrThrow(_stmt, "profile_name")
        val _columnIndexOfProfileType: Int = getColumnIndexOrThrow(_stmt, "profile_type")
        val _columnIndexOfStatName: Int = getColumnIndexOrThrow(_stmt, "stat_name")
        val _columnIndexOfStatValue: Int = getColumnIndexOrThrow(_stmt, "stat_value")
        val _result: MutableList<CharacteristicEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CharacteristicEntity
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpProfileName: String
          _tmpProfileName = _stmt.getText(_columnIndexOfProfileName)
          val _tmpProfileType: String
          _tmpProfileType = _stmt.getText(_columnIndexOfProfileType)
          val _tmpStatName: String
          _tmpStatName = _stmt.getText(_columnIndexOfStatName)
          val _tmpStatValue: String
          _tmpStatValue = _stmt.getText(_columnIndexOfStatValue)
          _item = CharacteristicEntity(_tmpGamesystemId,_tmpFactionId,_tmpSingularityId,_tmpProfileName,_tmpProfileType,_tmpStatName,_tmpStatValue)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getWargearGroupsForModel(
    gsId: String,
    fId: String,
    modelId: String,
  ): Flow<List<SingularityEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM singularities
        |        WHERE gamesystem_id = ? AND faction_id = ? 
        |        AND parent_id = ? AND xml_tag = 'selectionEntryGroup'
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, modelId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfXmlTag: Int = getColumnIndexOrThrow(_stmt, "xml_tag")
        val _columnIndexOfEntryType: Int = getColumnIndexOrThrow(_stmt, "entry_type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfLinkType: Int = getColumnIndexOrThrow(_stmt, "link_type")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfGamesystemName: Int = getColumnIndexOrThrow(_stmt, "gamesystem_name")
        val _columnIndexOfFactionName: Int = getColumnIndexOrThrow(_stmt, "faction_name")
        val _columnIndexOfIsAwakened: Int = getColumnIndexOrThrow(_stmt, "is_awakened")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpXmlTag: String
          _tmpXmlTag = _stmt.getText(_columnIndexOfXmlTag)
          val _tmpEntryType: String?
          if (_stmt.isNull(_columnIndexOfEntryType)) {
            _tmpEntryType = null
          } else {
            _tmpEntryType = _stmt.getText(_columnIndexOfEntryType)
          }
          val _tmpValue: String?
          if (_stmt.isNull(_columnIndexOfValue)) {
            _tmpValue = null
          } else {
            _tmpValue = _stmt.getText(_columnIndexOfValue)
          }
          val _tmpParentId: String?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getText(_columnIndexOfParentId)
          }
          val _tmpTargetId: String?
          if (_stmt.isNull(_columnIndexOfTargetId)) {
            _tmpTargetId = null
          } else {
            _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          }
          val _tmpLinkType: String?
          if (_stmt.isNull(_columnIndexOfLinkType)) {
            _tmpLinkType = null
          } else {
            _tmpLinkType = _stmt.getText(_columnIndexOfLinkType)
          }
          val _tmpCategory: String?
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          }
          val _tmpGamesystemName: String
          _tmpGamesystemName = _stmt.getText(_columnIndexOfGamesystemName)
          val _tmpFactionName: String
          _tmpFactionName = _stmt.getText(_columnIndexOfFactionName)
          val _tmpIsAwakened: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsAwakened).toInt()
          _tmpIsAwakened = _tmp != 0
          _item = SingularityEntity(_tmpId,_tmpGamesystemId,_tmpFactionId,_tmpName,_tmpXmlTag,_tmpEntryType,_tmpValue,_tmpParentId,_tmpTargetId,_tmpLinkType,_tmpCategory,_tmpGamesystemName,_tmpFactionName,_tmpIsAwakened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getCostsForSingularity(
    gsId: String,
    fId: String,
    singularityId: String,
  ): Flow<List<CostEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM costs
        |        WHERE gamesystem_id = ? AND faction_id = ? AND singularity_id = ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("costs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, singularityId)
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _result: MutableList<CostEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CostEntity
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpValue: Double
          _tmpValue = _stmt.getDouble(_columnIndexOfValue)
          _item = CostEntity(_tmpGamesystemId,_tmpFactionId,_tmpSingularityId,_tmpName,_tmpValue)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getCategoryLinksForSingularity(
    gsId: String,
    fId: String,
    singularityId: String,
  ): Flow<List<CategoryLinkEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM category_links
        |        WHERE gamesystem_id = ? AND faction_id = ? AND singularity_id = ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("category_links")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, singularityId)
        val _columnIndexOfGamesystemId: Int = getColumnIndexOrThrow(_stmt, "gamesystem_id")
        val _columnIndexOfFactionId: Int = getColumnIndexOrThrow(_stmt, "faction_id")
        val _columnIndexOfSingularityId: Int = getColumnIndexOrThrow(_stmt, "singularity_id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "target_id")
        val _columnIndexOfCategoryName: Int = getColumnIndexOrThrow(_stmt, "category_name")
        val _columnIndexOfIsPrimary: Int = getColumnIndexOrThrow(_stmt, "is_primary")
        val _result: MutableList<CategoryLinkEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryLinkEntity
          val _tmpGamesystemId: String
          _tmpGamesystemId = _stmt.getText(_columnIndexOfGamesystemId)
          val _tmpFactionId: String
          _tmpFactionId = _stmt.getText(_columnIndexOfFactionId)
          val _tmpSingularityId: String
          _tmpSingularityId = _stmt.getText(_columnIndexOfSingularityId)
          val _tmpTargetId: String
          _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          val _tmpCategoryName: String
          _tmpCategoryName = _stmt.getText(_columnIndexOfCategoryName)
          val _tmpIsPrimary: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsPrimary).toInt()
          _tmpIsPrimary = _tmp != 0
          _item = CategoryLinkEntity(_tmpGamesystemId,_tmpFactionId,_tmpSingularityId,_tmpTargetId,_tmpCategoryName,_tmpIsPrimary)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getKeywordsForUnit(
    gsId: String,
    fId: String,
    singularityId: String,
  ): Flow<List<String>> {
    val _sql: String = """
        |
        |        SELECT category_name FROM category_links
        |        WHERE gamesystem_id = ? AND faction_id = ? AND singularity_id = ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("category_links")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, fId)
        _argIndex = 3
        _stmt.bindText(_argIndex, singularityId)
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearFactionData(gamesystemId: String, factionId: String) {
    val _sql: String = "DELETE FROM singularities WHERE gamesystem_id = ? AND faction_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gamesystemId)
        _argIndex = 2
        _stmt.bindText(_argIndex, factionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllArgonauts() {
    val _sql: String = "DELETE FROM argonauts"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllRosters() {
    val _sql: String = "DELETE FROM rosters"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllSingularities() {
    val _sql: String = "DELETE FROM singularities"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllTags() {
    val _sql: String = "DELETE FROM singularity_tags"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllCategoryLinks() {
    val _sql: String = "DELETE FROM category_links"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllCosts() {
    val _sql: String = "DELETE FROM costs"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllCharacteristics() {
    val _sql: String = "DELETE FROM characteristics"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllConstraints() {
    val _sql: String = "DELETE FROM constraints"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllSyncMetadata() {
    val _sql: String = "DELETE FROM sync_metadata"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAllGameSystemMetadata() {
    val _sql: String = "DELETE FROM game_system_metadata"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteRoster(id: String) {
    val _sql: String = "DELETE FROM rosters WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteArgonaut(id: String) {
    val _sql: String = "DELETE FROM argonauts WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateLastUpdated(gsId: String, timestamp: Long) {
    val _sql: String = "UPDATE game_system_metadata SET lastUpdated = ? WHERE repoName = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        _argIndex = 2
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearSystemSingularities(gsId: String) {
    val _sql: String = "DELETE FROM singularities WHERE gamesystem_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearSystemTags(gsId: String) {
    val _sql: String = "DELETE FROM singularity_tags WHERE gamesystem_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearSystemCategoryLinks(gsId: String) {
    val _sql: String = "DELETE FROM category_links WHERE gamesystem_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearSystemCosts(gsId: String) {
    val _sql: String = "DELETE FROM costs WHERE gamesystem_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearSystemCharacteristics(gsId: String) {
    val _sql: String = "DELETE FROM characteristics WHERE gamesystem_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearSystemConstraints(gsId: String) {
    val _sql: String = "DELETE FROM constraints WHERE gamesystem_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearSystemSyncMetadata(gsId: String) {
    val _sql: String = "DELETE FROM sync_metadata WHERE gamesystem_id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteSystemMetadata(repoName: String) {
    val _sql: String = "DELETE FROM game_system_metadata WHERE repoName = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, repoName)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pruneOrphanMetadata(gsId: String, activePaths: List<String>) {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("DELETE FROM sync_metadata WHERE gamesystem_id = ")
    _stringBuilder.append("?")
    _stringBuilder.append(" AND file_path NOT IN (")
    val _inputSize: Int = activePaths.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        for (_item: String in activePaths) {
          _stmt.bindText(_argIndex, _item)
          _argIndex++
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pruneOrphanSingularities(gsId: String) {
    val _sql: String = """
        |
        |        DELETE FROM singularities 
        |        WHERE gamesystem_id = ? 
        |        AND faction_id NOT IN (SELECT faction_id FROM sync_metadata WHERE gamesystem_id = ?)
        |        AND faction_id != 'CORE'
        |    
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gsId)
        _argIndex = 2
        _stmt.bindText(_argIndex, gsId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
