package com.example.helpinghands.data.model

data class Booking(
    val id: String = "",
    val userId: String = "",
    val serviceId: String = "",
    val plumberId: String = "",
    val date: String = "",
    val status: String = "Pending" // Pending, Confirmed, Completed, Cancelled
)
