package com.example.battlebarge

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

/**
 * UserRepository provides a stable, lifecycle-aware access point for user data.
 * It implements a "Single Source of Truth" pattern using a shared StateFlow.
 */
object UserRepository {
    private const val TAG = "UserRepository"
    
    internal var dbProvider: () -> FirebaseFirestore = { BargeDatabase.db }
    internal var authProvider: () -> FirebaseAuth = { BargeDatabase.auth }
    
    private val db get() = dbProvider()
    private val auth get() = authProvider()

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    private const val NETWORK_TIMEOUT = 15000L // Increased for better handling of slow connections

    // Cache of the current user's profile and UID for consistency
    @Volatile
    private var cachedProfile: UserProfile? = null
    @Volatile
    private var currentUid: String? = null
    
    // Tracks active initialization jobs to prevent loops
    private val initializationJobs = mutableMapOf<String, Job>()

    /**
     * Exposes the current authentication state as a Flow of UIDs.
     */
    val authStateFlow: Flow<String?> by lazy {
        callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val uid = firebaseAuth.currentUser?.uid
                trySend(uid)
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }.stateIn(repositoryScope, SharingStarted.Eagerly, auth.currentUser?.uid)
    }

    /**
     * A reactive stream of the user's profile data.
     * Uses flatMapLatest to switch listeners when the user logs in/out.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val userProfileFlow: StateFlow<UserProfile?> by lazy {
        authStateFlow
            .flatMapLatest { uid ->
                if (uid == null) {
                    cachedProfile = null
                    currentUid = null
                    flowOf(null)
                } else {
                    observeProfile(uid)
                }
            }
            .stateIn(repositoryScope, SharingStarted.WhileSubscribed(5000), null)
    }

    private fun observeProfile(uid: String): Flow<UserProfile?> = callbackFlow {
        Log.d(TAG, "Starting observation for UID: $uid")
        val docRef = db.collection("users").document(uid)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error observing profile for $uid", error)
                return@addSnapshotListener
            }
            
            if (snapshot != null && snapshot.exists()) {
                val profile = snapshot.toObject(UserProfile::class.java)?.copy(uid = uid)
                Log.d(TAG, "Profile received for $uid: ${profile?.username}")
                if (auth.currentUser?.uid == uid) {
                    cachedProfile = profile
                    currentUid = uid
                    trySend(profile)
                }
            } else {
                Log.w(TAG, "Profile snapshot does not exist for $uid.")
                val currentAuthUid = auth.currentUser?.uid
                if (currentAuthUid == uid) {
                    synchronized(initializationJobs) {
                        if (initializationJobs[uid]?.isActive != true) {
                            Log.w(TAG, "Triggering one-time initialization job for $uid")
                            initializationJobs[uid] = repositoryScope.launch {
                                initializeThemedProfile()
                            }
                        }
                    }
                }
                trySend(null)
            }
        }
        awaitClose { 
            Log.d(TAG, "Closing observation for UID: $uid")
            registration.remove() 
        }
    }

    /**
     * Synchronous access to the cached profile, validated against the current UID.
     */
    fun getCachedProfile(): UserProfile? {
        val uid = auth.currentUser?.uid
        return if (uid != null && uid == currentUid) cachedProfile else null
    }

    fun getCachedUsername(): String? = getCachedProfile()?.username

    /**
     * Updates the user's profile fields with validation and network timeouts.
     */
    suspend fun updateProfile(updates: Map<String, Any>): Result<Unit> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext Result.failure(IllegalStateException("User not authenticated"))

        try {
            // 1. Fetch current data if cache is missing to ensure validation context
            // We use a slightly shorter timeout for pre-fetch to maintain responsiveness
            val currentProfile = getCachedProfile() ?: withTimeoutOrNull(5000L) { fetchProfile(uid) }
            
            // 2. Perform deep validation on critical fields (can include username availability check)
            val validatedUpdates = validateUpdates(updates, currentProfile)
            
            // 3. Persist to Firestore with timeout
            try {
                withTimeout(NETWORK_TIMEOUT) {
                    db.collection("users").document(uid)
                        .set(validatedUpdates, SetOptions.merge())
                        .await()
                }
            } catch (_: TimeoutCancellationException) {
                // For non-critical profile updates, we treat a network timeout as success 
                // because Firestore performs an optimistic local update that will sync in the background.
                Log.w(TAG, "Profile update timed out on network, but local save is complete.")
            }

            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException && e !is TimeoutCancellationException) throw e
            Log.e(TAG, "Profile update failed", e)
            Result.failure(e)
        }
    }

    private suspend fun validateUpdates(updates: Map<String, Any>, current: UserProfile?): Map<String, Any> {
        val result = updates.toMutableMap()
        
        // Username logic: if blank or unchanged, use original or existing
        val requestedName = (updates["username"] as? String)?.trim()
        if (requestedName != null && requestedName != current?.username) {
            if (requestedName.isBlank()) {
                result["username"] = current?.originalUsername ?: ""
            } else {
                val validation = ProfanityFilter.validateUsername(requestedName)
                if (validation != UsernameValidationResult.Valid) {
                    throw IllegalArgumentException("Username rejected: ${validation.name}")
                }
                if (!isUsernameAvailable(requestedName)) {
                    throw IllegalStateException("Username already in use")
                }
            }
        }

        // Profanity sweep for other string fields
        updates.forEach { (key, value) ->
            if (key != "username" && value is String && value != current?.let { getFieldValue(it, key) }) {
                if (ProfanityFilter.containsProfanity(value)) {
                    throw IllegalArgumentException("Content in $key contains prohibited terms")
                }
            }
        }
        
        return result
    }

    private fun getFieldValue(profile: UserProfile, key: String): Any? = when(key) {
        "bio" -> profile.bio
        "region" -> profile.region
        else -> null
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        return try {
            withTimeout(5000L) {
                val query = db.collection("users")
                    .whereEqualTo("username", username)
                    .limit(1)
                    .get()
                    .await()
                query.isEmpty
            }
        } catch (_: Exception) {
            // Default to false on timeout/error to prevent accidental duplicates
            false
        }
    }

    /**
     * Initializes a default profile for new users.
     */
    suspend fun initializeThemedProfile(): Result<Unit> = withContext(Dispatchers.IO) {
        val uid = auth.currentUser?.uid ?: return@withContext Result.failure(Exception("No active session"))
        
        try {
            val existing = fetchProfile(uid)
            if (existing != null && existing.username.isNotBlank()) {
                return@withContext Result.success(Unit)
            }

            val prefixes = listOf("Vanguard", "Scout", "Prime", "Sentinel", "Oracle", "Warden")
            val suffixes = listOf("Alpha", "Omega", "Zero", "Echo", "Vector", "Ghost")
            
            var generatedName: String
            var attempts = 0
            do {
                generatedName = "${prefixes.random()}${suffixes.random()}${(100..999).random()}"
                attempts++
            } while (!isUsernameAvailable(generatedName) && attempts < 5)

            val initialData = UserProfile(
                uid = uid,
                username = generatedName,
                originalUsername = generatedName,
                createdAt = com.google.firebase.Timestamp.now()
            )

            db.collection("users").document(uid).set(initialData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchProfile(uid: String): UserProfile? = try {
        withTimeoutOrNull(5000L) {
            db.collection("users").document(uid).get().await().toObject(UserProfile::class.java)
        }
    } catch (_: Exception) {
        null
    }
}
