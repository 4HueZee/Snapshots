package com.battlebarge.agnostic.domain.model

data class RosterUnit(
    val id: String,
    val rosterId: String,
    val datasheetId: String,
    val gameSystemId: String,
    val factionId: String,
    val parentRosterUnitId: String? = null,
    val personalName: String,
    val modelCount: Int = 1,
    val points: Int = 0,
    val isWarlord: Boolean = false
)
