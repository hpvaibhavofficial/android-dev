package com.example.petcare.models

data class AdoptionRequest(
    val requestId: String = "",
    val petId: String = "",
    val userId: String = "",
    val status: String = "pending",
    val createdAt: Long = 0L
)
