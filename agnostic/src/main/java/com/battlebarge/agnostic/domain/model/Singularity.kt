package com.battlebarge.agnostic.domain.model

/**
 * The fundamental atomic unit of the Battle Barge engine.
 * Refactored for Native XML Relational Architecture.
 */
data class Singularity(
    val id: String,
    val name: String,
    val xmlTag: String,
    val entryType: String? = null,
    val value: String? = null,
    val parentId: String? = null,
    val targetId: String? = null,
    val linkType: String? = null,
    val category: String? = null,
    val tags: List<String> = emptyList(),
    
    // Scoping & Passport fields
    val gamesystemId: String,
    val gamesystemName: String,
    val factionId: String,
    val factionName: String,
    
    // Argonaut State
    val isAwakened: Boolean = false
)

/**
 * Complete Tangible Unit Datasheet Module assembled predictably from raw SQLite rows.
 */
data class UnitDatasheetModule(
    val unit: Singularity,
    val physicalStats: Map<String, String>,
    val mixedModelProfiles: Map<String, Map<String, String>> = emptyMap(),
    val rangedWeapons: List<WeaponProfile> = emptyList(),
    val meleeWeapons: List<WeaponProfile> = emptyList(),
    val abilities: List<AbilityRule> = emptyList()
)
