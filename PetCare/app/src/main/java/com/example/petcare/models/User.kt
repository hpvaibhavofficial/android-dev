package com.example.petcare.models

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "user",
    val city: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
