package com.battlebarge.agnostic.domain.model

/**
 * Sealed class representing the different states of the Singularity engine.
 */
sealed class SingularityState {
    object Idle : SingularityState()
    object Loading : SingularityState()
    data class Success(val lastSha: String?) : SingularityState()
    data class Error(val message: String) : SingularityState()
}
