package com.battlebarge.agnostic.domain.model

data class WeaponProfile(
    val id: String,
    val name: String,
    val range: String = "-",
    val attacks: String = "1",
    val skill: String = "3+",
    val strength: String = "4",
    val ap: String = "0",
    val damage: String = "1",
    val isRanged: Boolean = true,
    val keywords: List<String> = emptyList(),
    val targetId: String? = null
)
