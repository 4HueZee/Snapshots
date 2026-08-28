package com.example.battlebarge

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.Timestamp
import kotlinx.coroutines.*

/**
 * PresenceRepository tracks the user's online status in real-time.
 * It leverages the ProcessLifecycleOwner to detect when the app is in the foreground
 * or has been moved to the background.
 */
object PresenceRepository : DefaultLifecycleObserver {
    private const val TAG = "PresenceRepository"
    private val db get() = BargeDatabase.db
    private val auth get() = BargeDatabase.auth
    private val presenceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var isAppInForeground = false
    private var backgroundJob: Job? = null

    /**
     * Initializes the observer and sets up the Auth state listener.
     */
    fun initialize() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        
        // Ensure presence is updated immediately if user is already logged in
        auth.addAuthStateListener { firebaseAuth ->
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null && isAppInForeground) {
                updatePresence(PresenceConstants.ONLINE)
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        isAppInForeground = true
        backgroundJob?.cancel()
        updatePresence(PresenceConstants.ONLINE)
    }

    override fun onStop(owner: LifecycleOwner) {
        isAppInForeground = false
        // Update to AWAY immediately, then schedule RECENTLY_SEEN after 15m
        updatePresence(PresenceConstants.AWAY)
        
        backgroundJob?.cancel()
        backgroundJob = presenceScope.launch {
            delay(15 * 60 * 1000L) // 15 minutes
            if (!isAppInForeground) {
                updatePresence(PresenceConstants.RECENTLY_SEEN)
            }
        }
    }

    fun setOfflineManually() {
        updatePresence(PresenceConstants.OFFLINE)
    }

    private fun updatePresence(status: String) {
        val uid = auth.currentUser?.uid ?: return
        presenceScope.launch {
            try {
                db.collection("users").document(uid).update(
                    mapOf(
                        "isOnline" to (status == PresenceConstants.ONLINE),
                        "presenceStatus" to status,
                        "lastActive" to Timestamp.now()
                    )
                ).addOnFailureListener { e ->
                    Log.e(TAG, "Failed to update presence to $status", e)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in updatePresence ($status)", e)
            }
        }
    }
}
