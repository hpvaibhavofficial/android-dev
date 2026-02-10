package com.example.petcare.models

data class RequestUIModel(
    val requestId: String = "",
    val petId: String = "",
    val petName: String = "",
    val petImageUrl: String = "",
    val status: String = "pending",
    val createdAt: Long = 0L
)
