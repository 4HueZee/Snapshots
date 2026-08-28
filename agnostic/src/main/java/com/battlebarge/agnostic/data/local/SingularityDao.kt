package com.battlebarge.agnostic.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SingularityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingularities(singularities: List<SingularityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<SingularityTagEntity>)

    @Query("DELETE FROM singularities")
    suspend fun clearAllSingularities()

    @Query("SELECT * FROM singularities WHERE id = :id")
    suspend fun getSingularityById(id: String): SingularityEntity?

    @Query("SELECT * FROM singularities WHERE parent_id = :parentId")
    fun getChildrenOf(parentId: String): Flow<List<SingularityEntity>>

    @Query("SELECT tag FROM singularity_tags WHERE singularity_id = :singularityId")
    suspend fun getTagsForSingularity(singularityId: String): List<String>

    @Transaction
    suspend fun replaceData(singularities: List<SingularityEntity>, tags: List<SingularityTagEntity>) {
        clearAllSingularities()
        insertSingularities(singularities)
        insertTags(tags)
    }
}
