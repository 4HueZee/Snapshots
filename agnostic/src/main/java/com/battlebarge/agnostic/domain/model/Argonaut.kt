package com.battlebarge.agnostic.domain.model

import java.util.UUID

/**
 * The "Living Record" of a unit instance.
 * Inherits its base rules from a [Singularity] but holds personal configuration and points math.
 *
 * @property id Unique UUID for this specific miniature/unit instance.
 * @property rosterId The ID of the roster this Argonaut belongs to.
 * @property singularityId The ID of the base rule (Singularity) in the codex.
 * @property gamesystemId Scoping to ensure this Argonaut belongs to the right game.
 * @property factionId Scoping to ensure this Argonaut belongs to the right faction.
 * @property parentArgonautId Optional parent Argonaut ID for nested wargear/models.
 *
 * @property personalName The name given by the user (e.g., "Sgt. Aurelius").
 * @property designation The squad marking (e.g., "8th Battleline").
 * @property personality A temperament or trait (e.g., "Relentless").
 * @property imageUri Path to local photo.
 * @property isWarlord True if designated as the roster Warlord.
 * @property modelCount The current squad size / model count.
 * @property basePoints Base unit points cost.
 * @property modelCost Additional cost per model.
 * @property wargearPoints Total cost of child wargear upgrades.
 */
data class Argonaut(
    val id: String = UUID.randomUUID().toString(),
    val rosterId: String,
    val singularityId: String,
    val gamesystemId: String,
    val factionId: String,
    val parentArgonautId: String? = null,
    
    val personalName: String,
    val designation: String? = null,
    val personality: String? = null,
    val imageUri: String? = null,
    val isWarlord: Boolean = false,
    val modelCount: Int = 1,
    
    val basePoints: Int = 0,
    val modelCost: Int = 0,
    val wargearPoints: Int = 0,
    
    /**
     * The SHA of the rule file at the moment this unit was added/updated.
     * Used for Delta Awareness (detecting if rules have changed).
     */
    val ruleSha: String? = null
) {
    /**
     * Natively calculated total points for this Argonaut unit instance.
     */
    val totalPoints: Int
        get() = basePoints + ((modelCount - 1).coerceAtLeast(0) * modelCost) + wargearPoints
}
