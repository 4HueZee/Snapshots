package com.example.battlebarge

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

/**
 * Centralized provider for Firebase services to ensure consistent configuration
 * and prevent "settings already changed" errors.
 */
object BargeDatabase {
    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance().apply {
            try {
                val settings = FirebaseFirestoreSettings.Builder()
                    .setLocalCacheSettings(PersistentCacheSettings.newBuilder()
                        .setSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                        .build())
                    .build()
                firestoreSettings = settings
            } catch (_: IllegalStateException) {
                // Settings were already set or Firestore was already accessed.
            }
        }
    }
}
