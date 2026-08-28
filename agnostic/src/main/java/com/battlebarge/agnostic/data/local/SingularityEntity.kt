package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.battlebarge.agnostic.domain.model.Singularity

/**
 * Room entity representing a Singularity in the local "Eden" database.
 * We use indexing on [type] and [parentId] to speed up common tree traversal queries.
 */
@Entity(
    tableName = "singularities",
    indices = [
        Index(value = ["type"]),
        Index(value = ["parent_id"])
    ]
)
data class SingularityEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val value: String?,
    @ColumnInfo(name = "parent_id")
    val parentId: String?
)

/**
 * Extension to convert an Entity back to a Domain Model.
 * Tags are usually loaded separately in a joined query.
 */
fun SingularityEntity.toDomain(tags: List<String> = emptyList()): Singularity {
    return Singularity(
        id = id,
        name = name,
        type = type,
        value = value,
        parentId = parentId,
        tags = tags
    )
}

/**
 * Extension to convert a Domain Model to an Entity.
 */
fun Singularity.toEntity(): SingularityEntity {
    return SingularityEntity(
        id = id,
        name = name,
        type = type,
        value = value,
        parentId = parentId
    )
}
