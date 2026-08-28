package com.example.battlebarge

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockUser: FirebaseUser

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any<String>(), any<String>()) } returns 0
        every { Log.e(any<String>(), any<String>(), any()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0
        
        mockkStatic(FirebaseAuth::class)
        mockkStatic(FirebaseFirestore::class)

        mockAuth = mockk(relaxed = true)
        mockFirestore = mockk(relaxed = true)
        mockUser = mockk(relaxed = true)

        UserRepository.authProvider = { mockAuth }
        UserRepository.dbProvider = { mockFirestore }
        
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "test-uid"
    }

    @Test
    fun `test profile update failure when not authenticated`() {
        runTest {
            every { mockAuth.currentUser } returns null
            
            val result = UserRepository.updateProfile(mapOf("bio" to "New Bio"))
            
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is IllegalStateException)
        }
    }

    @Test
    fun `test isUsernameAvailable returns false on exception`() {
        runTest {
            val mockCollection = mockk<CollectionReference>()
            val mockQuery = mockk<Query>()
            
            every { mockFirestore.collection("users") } returns mockCollection
            every { mockCollection.whereEqualTo("username", any()) } returns mockQuery
            every { mockQuery.limit(1) } returns mockQuery
            every { mockQuery.get() } throws Exception("Firestore Error")
            
            val available = UserRepository.isUsernameAvailable("TestUser")
            
            assertFalse(available)
        }
    }
}
