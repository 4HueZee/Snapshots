package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.battlebarge.agnostic.domain.model.PhysicalStats
import com.battlebarge.agnostic.domain.model.UnitDatasheet

@Entity(
    tableName = "unit_datasheets",
    indices = [
        Index(value = ["gamesystem_id"]),
        Index(value = ["faction_id"]),
        Index(value = ["gamesystem_id", "faction_id"]),
        Index(value = ["category"])
    ]
)
data class UnitDatasheetEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "gamesystem_id")
    val gamesystemId: String,
    @ColumnInfo(name = "faction_id")
    val factionId: String,
    val name: String,
    val category: String,
    @ColumnInfo(name = "base_points")
    val basePoints: Int = 0,
    val movement: String = "6\"",
    val toughness: String = "4",
    val save: String = "3+",
    val wounds: String = "2",
    val leadership: String = "6+",
    @ColumnInfo(name = "objective_control")
    val objectiveControl: String = "2",
    val keywords: String = ""
)

fun UnitDatasheetEntity.toDomain(): UnitDatasheet {
    return UnitDatasheet(
        id = id,
        name = name,
        gameSystemId = gamesystemId,
        factionId = factionId,
        category = category,
        basePoints = basePoints,
        stats = PhysicalStats(
            movement = movement,
            toughness = toughness,
            save = save,
            wounds = wounds,
            leadership = leadership,
            objectiveControl = objectiveControl
        ),
        keywords = if (keywords.isBlank()) emptyList() else keywords.split(",")
    )
}

fun UnitDatasheet.toEntity(): UnitDatasheetEntity {
    return UnitDatasheetEntity(
        id = id,
        gamesystemId = gameSystemId,
        factionId = factionId,
        name = name,
        category = category,
        basePoints = basePoints,
        movement = stats.movement,
        toughness = stats.toughness,
        save = stats.save,
        wounds = stats.wounds,
        leadership = stats.leadership,
        objectiveControl = stats.objectiveControl,
        keywords = keywords.joinToString(",")
    )
}
