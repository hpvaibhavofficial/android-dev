package com.example.helpinghands.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stockQuantity: Int = 0,
    val categoryId: String = "",
    val imageUrl: String? = null,
    val isFeatured: Boolean = false
)