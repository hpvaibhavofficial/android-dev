package com.example.petcare.models

data class Pet(
    val petId: String = "",
    val name: String = "",
    val type: String = "",
    val breed: String = "",
    val age: Int = 0,
    val gender: String = "",
    val price: Double = 0.0,
    val status: String = "available",
    val tags: List<String> = emptyList(),
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
