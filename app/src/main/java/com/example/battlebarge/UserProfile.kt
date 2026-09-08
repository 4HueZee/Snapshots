package com.example.battlebarge

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

/**
 * Domain model representing a User's Profile in the Battle Barge ecosystem.
 * Following industry standards, this class uses default values for Firestore mapping.
 */
data class UserProfile(
    val uid: String = "",
    val username: String = "",
    val originalUsername: String = "",
    val bio: String = "",
    val region: String = "NA",
    @get:PropertyName("isPublic") @set:PropertyName("isPublic") var isPublic: Boolean = true,
    @get:PropertyName("isOnline") @set:PropertyName("isOnline") var isOnline: Boolean = false,
    val presenceStatus: String = "OFFLINE",
    val lastActive: Timestamp? = null,
    val createdAt: Timestamp? = null
)

object PresenceConstants {
    const val ONLINE = "ONLINE"
    const val AWAY = "AWAY"
    const val RECENTLY_SEEN = "RECENTLY_SEEN"
    const val OFFLINE = "OFFLINE"
}
