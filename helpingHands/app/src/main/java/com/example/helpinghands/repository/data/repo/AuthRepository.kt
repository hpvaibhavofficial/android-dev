package com.example.helpinghands.repository.data.repo

import com.example.helpinghands.data.model.User

/**
 * Interface defining the Authentication operations (Login, Signup, Role Check).
 * This layer abstracts the Firebase API away from the ViewModel.
 */
interface AuthRepository {
    val currentUserId: String?
    suspend fun signUp(email: String, password: String, name: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun getUserRole(uid: String): Result<User>
    fun signOut()
}