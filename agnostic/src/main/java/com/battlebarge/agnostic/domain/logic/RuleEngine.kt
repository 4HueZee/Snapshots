package com.battlebarge.agnostic.domain.logic

import com.battlebarge.agnostic.domain.model.Argonaut
import com.battlebarge.agnostic.domain.model.Singularity
import com.battlebarge.agnostic.domain.repository.SingularityRepository

/**
 * The "Brain" for rule resolution.
 * Implements Lazy Evaluation and Memoization for complex game rules.
 */
class RuleEngine(
    private val repository: SingularityRepository
) {
    private val pointsCache = mutableMapOf<String, Int>()

    /**
     * Calculates the points for an Argonaut selection.
     * Uses Memoization to avoid re-calculating the same node multiple times.
     */
    suspend fun resolvePoints(argonaut: Argonaut): Int {
        // High-Performance Strategy: Only evaluate what is requested (Lazy)
        val cached = pointsCache[argonaut.id]
        if (cached != null) return cached

        val totalPoints = calculateRecursivePoints(
            argonaut.gamesystemId,
            argonaut.factionId,
            argonaut.singularityId
        )
        
        // Memoize the result to ensure sub-5ms performance on subsequent UI frames
        pointsCache[argonaut.id] = totalPoints
        return totalPoints
    }

    private suspend fun calculateRecursivePoints(gsId: String, fId: String, nodeId: String): Int {
        val node = repository.getSingularityById(gsId, fId, nodeId) ?: return 0
        var sum = node.value?.toIntOrNull() ?: 0

        // Traverse children using indexed lookups
        val children = repository.getChildren(gsId, fId, nodeId)
        children.forEach { child ->
            sum += calculateRecursivePoints(gsId, fId, child.id)
        }
        
        return sum
    }

    /**
     * Detects if an Argonaut's rules have changed since it was created.
     */
    suspend fun isOutdated(argonaut: Argonaut): Boolean {
        if (argonaut.ruleSha == null) return false
        
        val latestSha = repository.getLatestShaForFaction(
            argonaut.gamesystemId, 
            argonaut.factionId
        )
        
        return latestSha != null && latestSha != argonaut.ruleSha
    }

    /**
     * Clears the cache for a specific selection when it changes.
     */
    fun invalidate(argonautId: String) {
        pointsCache.remove(argonautId)
    }

    /**
     * Checks if any units in a roster are outdated.
     * Useful for showing a top-level "Update Available" notification for an army.
     */
    suspend fun validateRoster(argonauts: List<Argonaut>): Boolean {
        return argonauts.any { isOutdated(it) }
    }
}
