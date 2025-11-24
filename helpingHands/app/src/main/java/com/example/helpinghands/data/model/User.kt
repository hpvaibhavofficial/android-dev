package com.example.helpinghands.data.model

//data class User(
//    val uid: String = "",
//    val name: String = "",
//    val email: String = "",
//    val phone: String = "",
//    val address: String = "",
//    val userType: String = "customer" // could be "customer" or "plumber"
//)

/**
 * Defines the user profile structure stored in Firebase Firestore.
 * The 'isAdmin' field is critical for role-based routing.
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val imageUrl: String = "",
    val isAdmin: Boolean = false
)
