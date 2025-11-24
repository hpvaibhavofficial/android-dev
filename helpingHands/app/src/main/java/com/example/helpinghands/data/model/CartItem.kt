package com.example.helpinghands.data.model

data class CartItem(
    val productId: String = "",
    val name: String = "", // Denormalized for quick display
    val price: Double = 0.0, // Denormalized price at the time of adding
    val quantity: Int = 1
)