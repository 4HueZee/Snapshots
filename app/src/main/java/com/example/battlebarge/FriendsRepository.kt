package com.example.battlebarge

import android.util.Log
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

data class FriendRequest(
    val id: String,
    val senderId: String,
    val senderUsername: String,
    val timestamp: Long
)

object FriendsRepository {
    private val db get() = BargeDatabase.db
    private val auth get() = BargeDatabase.auth
    private val socialScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Shared flow for friends list. Real-time updates with zero redundant fetches.
     */
    val friendsFlow: Flow<List<UserProfile>> by lazy {
        callbackFlow {
            val uid = auth.currentUser?.uid ?: run {
                trySend(emptyList())
                return@callbackFlow
            }

            var userListener: com.google.firebase.firestore.ListenerRegistration? = null

            val friendsSubListener = db.collection("users").document(uid).collection("friends")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("FriendsRepository", "Friends Flow Error: ${error.message}")
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val friendIds = snapshot?.documents?.map { it.id } ?: emptyList()
                    
                    userListener?.remove()

                    if (friendIds.isEmpty()) {
                        trySend(emptyList())
                    } else {
                        userListener = db.collection("users")
                            .whereIn("uid", friendIds.take(30))
                            .addSnapshotListener { userSnapshot, userError ->
                                if (userError != null) return@addSnapshotListener
                                val friends = userSnapshot?.documents?.mapNotNull { 
                                    it.toObject(UserProfile::class.java)?.copy(uid = it.id) 
                                } ?: emptyList()
                                trySend(friends)
                            }
                    }
                }
            awaitClose { 
                friendsSubListener.remove()
                userListener?.remove()
            }
        }.shareIn(socialScope, SharingStarted.WhileSubscribed(5000), replay = 1)
    }

    /**
     * Shared flow for pending requests.
     */
    val requestsFlow: Flow<List<FriendRequest>> by lazy {
        callbackFlow {
            val uid = auth.currentUser?.uid ?: run {
                trySend(emptyList())
                return@callbackFlow
            }

            val listener = db.collection("friend_requests")
                .whereEqualTo("receiverId", uid)
                .whereEqualTo("status", "pending")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("FriendsRepository", "Requests Flow Error: ${error.message}")
                        // Don't close the flow, just send empty to keep UI stable
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val requests = snapshot?.documents?.map { doc ->
                        FriendRequest(
                            id = doc.id,
                            senderId = doc.getString("senderId") ?: "",
                            senderUsername = doc.getString("senderUsername") ?: "Unknown",
                            timestamp = doc.getTimestamp("timestamp")?.seconds ?: 0L
                        )
                    } ?: emptyList()
                    trySend(requests)
                }
            awaitClose { listener.remove() }
        }.shareIn(socialScope, SharingStarted.WhileSubscribed(5000), replay = 1)
    }

    suspend fun searchUserByUsername(username: String): UserProfile? {
        return try {
            val query = db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()
            
            if (query.isEmpty) null else {
                val doc = query.documents.first()
                UserProfile(doc.id, doc.getString("username") ?: "Unknown")
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun sendFriendRequest(targetUid: String, targetUsername: String): Result<Unit> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("Not logged in"))
        
        return try {
            // Efficiency: Read from local memory cache first
            val currentUsername = UserRepository.getCachedUsername() ?: "Operative"

            if (currentUser.uid == targetUid) return Result.failure(Exception("Cannot add yourself"))

            val requestId = "${currentUser.uid}_$targetUid"
            val request = mapOf(
                "senderId" to currentUser.uid,
                "senderUsername" to currentUsername,
                "receiverId" to targetUid,
                "timestamp" to FieldValue.serverTimestamp(),
                "status" to "pending"
            )

            db.collection("friend_requests").document(requestId).set(request).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("Not logged in"))
        
        return try {
            val requestDoc = db.collection("friend_requests").document(requestId).get().await()
            if (!requestDoc.exists()) return Result.failure(Exception("Request not found"))

            val senderId = requestDoc.getString("senderId") ?: ""
            val senderUsername = requestDoc.getString("senderUsername") ?: ""
            val receiverUsername = UserRepository.getCachedUsername() ?: "Operative"

            if (accept) {
                val batch = db.batch()
                
                val currentUserFriendRef = db.collection("users").document(currentUser.uid)
                    .collection("friends").document(senderId)
                batch.set(currentUserFriendRef, mapOf("username" to senderUsername, "addedAt" to FieldValue.serverTimestamp()))

                val senderUserFriendRef = db.collection("users").document(senderId)
                    .collection("friends").document(currentUser.uid)
                batch.set(senderUserFriendRef, mapOf("username" to receiverUsername, "addedAt" to FieldValue.serverTimestamp()))

                batch.delete(db.collection("friend_requests").document(requestId))
                batch.commit().await()
            } else {
                db.collection("friend_requests").document(requestId).delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
