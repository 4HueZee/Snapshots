package com.battlebarge.agnostic.domain.model

data class AbilityRule(
    val id: String,
    val name: String,
    val description: String? = null,
    val targetId: String? = null
)
