package com.battlebarge.agnostic.domain.model

/**
 * Sealed class representing the different states of the Singularity engine.
 */
sealed class SingularityState {
    data object Idle : SingularityState()
    
    /**
     * @property message The current task being performed (e.g. "Queued", "Downloading Orks")
     * @property queueSize Number of items waiting in the queue.
     * @property gsId Optional Game System ID being processed.
     */
    data class Loading(
        val message: String = "Initializing...",
        val queueSize: Int = 0,
        val gsId: String? = null
    ) : SingularityState()

    /**
     * State for bulk operations (Arsenal Sync).
     * @property gsId Optional Game System ID being processed.
     */
    data class BulkLoading(
        val message: String,
        val progress: Float,
        val current: Int,
        val total: Int,
        val gsId: String? = null
    ) : SingularityState()
    
    /**
     * @property lastSha The SHA of the file retrieved.
     * @property gsId The Game System ID of the synced data.
     * @property fId The Faction ID of the synced data.
     */
    data class Success(
        val lastSha: String?,
        val gsId: String? = null,
        val fId: String? = null
    ) : SingularityState()
    
    data class Error(val message: String) : SingularityState()
}
