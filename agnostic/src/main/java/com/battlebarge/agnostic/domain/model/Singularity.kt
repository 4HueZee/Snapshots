package com.battlebarge.agnostic.domain.model

/**
 * The fundamental atomic unit of the Battle Barge agnostic engine.
 * A Singularity can represent any game element: a unit, a rule, a stat, or an upgrade.
 *
 * @property id The unique identifier (usually from the source XML/GitHub).
 * @property name The display name of the element.
 * @property type The classification of the element (e.g., "unit", "stat", "weapon").
 * @property value An optional value associated with the element (e.g., the value of a "Strength" stat).
 * @property parentId The ID of the parent Singularity, maintaining the tree structure of the roster.
 * @property tags A list of metadata tags for filtering, grouping, and validation logic.
 */
data class Singularity(
    val id: String,
    val name: String,
    val type: String,
    val value: String? = null,
    val parentId: String? = null,
    val tags: List<String> = emptyList()
)
