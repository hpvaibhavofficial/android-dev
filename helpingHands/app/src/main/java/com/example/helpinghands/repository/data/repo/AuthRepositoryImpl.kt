package com.example.helpinghands.repository.data.repo

import com.example.helpinghands.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of AuthRepository that interacts directly with Firebase services (Auth and Firestore).
 * It uses Kotlin Coroutines (.await()) for safe asynchronous operations.
 */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    // Reference to the Firestore collection where user profiles and roles are stored
    private val usersCollection = firestore.collection("users")

    override val currentUserId: String? get() = auth.currentUser?.uid

    // --- Helper to fetch User profile from Firestore (needed by both sign-in and session check) ---
    private suspend fun getAndVerifyUser(uid: String): Result<User> {
        return try {
            val snapshot = usersCollection.document(uid).get().await()
            // Map the Firestore document to our local User data class
            val user = snapshot.toObject(User::class.java)

            if (user != null) {
                Result.success(user)
            } else {
                // If user auth exists but the profile document doesn't (critical error)
                Result.failure(Exception("User profile not found in database."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            // 1. Create user in Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("User creation failed in Firebase Auth.")

            // 2. Create the default Customer profile (isAdmin = false) in Firestore
            val newUser = User(uid = firebaseUser.uid, email = email, isAdmin = false, name = name)
            usersCollection.document(firebaseUser.uid).set(newUser).await()

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            // 1. Sign in with email/password
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Login failed: Invalid credentials.")

            // 2. Fetch the assigned role (isAdmin flag) for role-based routing
            getAndVerifyUser(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserRole(uid: String): Result<User> = withContext(Dispatchers.IO) {
        getAndVerifyUser(uid)
    }

    override fun signOut() {
        auth.signOut()
    }
}