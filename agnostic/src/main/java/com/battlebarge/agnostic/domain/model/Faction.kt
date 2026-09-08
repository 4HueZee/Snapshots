package com.battlebarge.agnostic.domain.model

data class Faction(
    val id: String,
    val name: String,
    val gameSystemId: String,
    val gameSystemName: String = ""
)
