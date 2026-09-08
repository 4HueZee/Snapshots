package com.battlebarge.agnostic.domain.model

data class Roster(
    val id: String,
    val name: String,
    val gameSystemId: String,
    val factionId: String,
    val pointsLimit: Int = 2000,
    val totalPoints: Int = 0,
    val units: List<RosterUnit> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
