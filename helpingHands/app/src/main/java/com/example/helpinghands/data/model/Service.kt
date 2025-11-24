package com.example.helpinghands.data.model

data class Service(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val basePrice: Double = 0.0,
    val estimatedDurationHours: Int = 1,
    val imageUrl: String? = null   // ⭐ REQUIRED for displaying image
)
