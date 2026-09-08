package com.battlebarge.agnostic.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.battlebarge.agnostic.domain.model.RosterUnit

@Entity(
    tableName = "roster_units",
    indices = [
        Index(value = ["roster_id"]),
        Index(value = ["datasheet_id"])
    ]
)
data class RosterUnitEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "roster_id")
    val rosterId: String,
    @ColumnInfo(name = "datasheet_id")
    val datasheetId: String,
    @ColumnInfo(name = "gamesystem_id")
    val gamesystemId: String,
    @ColumnInfo(name = "faction_id")
    val factionId: String,
    @ColumnInfo(name = "parent_roster_unit_id")
    val parentRosterUnitId: String? = null,
    @ColumnInfo(name = "personal_name")
    val personalName: String,
    @ColumnInfo(name = "model_count")
    val modelCount: Int = 1,
    val points: Int = 0,
    @ColumnInfo(name = "is_warlord")
    val isWarlord: Boolean = false
)

fun RosterUnitEntity.toDomain(): RosterUnit {
    return RosterUnit(
        id = id,
        rosterId = rosterId,
        datasheetId = datasheetId,
        gameSystemId = gamesystemId,
        factionId = factionId,
        parentRosterUnitId = parentRosterUnitId,
        personalName = personalName,
        modelCount = modelCount,
        points = points,
        isWarlord = isWarlord
    )
}

fun RosterUnit.toEntity(): RosterUnitEntity {
    return RosterUnitEntity(
        id = id,
        rosterId = rosterId,
        datasheetId = datasheetId,
        gamesystemId = gameSystemId,
        factionId = factionId,
        parentRosterUnitId = parentRosterUnitId,
        personalName = personalName,
        modelCount = modelCount,
        points = points,
        isWarlord = isWarlord
    )
}
