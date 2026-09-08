package com.battlebarge.agnostic.domain.model

data class UnitDatasheet(
    val id: String,
    val name: String,
    val gameSystemId: String,
    val factionId: String,
    val category: String,
    val basePoints: Int = 0,
    val stats: PhysicalStats = PhysicalStats(),
    val weapons: List<WeaponProfile> = emptyList(),
    val abilities: List<AbilityRule> = emptyList(),
    val keywords: List<String> = emptyList()
)
