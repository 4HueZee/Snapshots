package com.battlebarge.agnostic.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SingularityDao_Impl(
  __db: RoomDatabase,
) : SingularityDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSingularityEntity: EntityInsertAdapter<SingularityEntity>

  private val __insertAdapterOfSingularityTagEntity: EntityInsertAdapter<SingularityTagEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSingularityEntity = object : EntityInsertAdapter<SingularityEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `singularities` (`id`,`name`,`type`,`value`,`parent_id`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SingularityEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.type)
        val _tmpValue: String? = entity.value
        if (_tmpValue == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpValue)
        }
        val _tmpParentId: String? = entity.parentId
        if (_tmpParentId == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpParentId)
        }
      }
    }
    this.__insertAdapterOfSingularityTagEntity = object : EntityInsertAdapter<SingularityTagEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `singularity_tags` (`singularity_id`,`tag`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SingularityTagEntity) {
        statement.bindText(1, entity.singularityId)
        statement.bindText(2, entity.tag)
      }
    }
  }

  public override suspend fun insertSingularities(singularities: List<SingularityEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSingularityEntity.insert(_connection, singularities)
  }

  public override suspend fun insertTags(tags: List<SingularityTagEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSingularityTagEntity.insert(_connection, tags)
  }

  public override suspend fun replaceData(singularities: List<SingularityEntity>, tags: List<SingularityTagEntity>): Unit = performInTransactionSuspending(__db) {
    super@SingularityDao_Impl.replaceData(singularities, tags)
  }

  public override suspend fun getSingularityById(id: String): SingularityEntity? {
    val _sql: String = "SELECT * FROM singularities WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _result: SingularityEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
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
          _result = SingularityEntity(_tmpId,_tmpName,_tmpType,_tmpValue,_tmpParentId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getChildrenOf(parentId: String): Flow<List<SingularityEntity>> {
    val _sql: String = "SELECT * FROM singularities WHERE parent_id = ?"
    return createFlow(__db, false, arrayOf("singularities")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, parentId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfValue: Int = getColumnIndexOrThrow(_stmt, "value")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parent_id")
        val _result: MutableList<SingularityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SingularityEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
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
          _item = SingularityEntity(_tmpId,_tmpName,_tmpType,_tmpValue,_tmpParentId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTagsForSingularity(singularityId: String): List<String> {
    val _sql: String = "SELECT tag FROM singularity_tags WHERE singularity_id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
