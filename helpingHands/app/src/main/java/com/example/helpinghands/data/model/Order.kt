package com.example.helpinghands.data.model

import com.google.firebase.Timestamp
import java.io.Serializable

data class Order(
    val id: String = "",
    val serviceName: String = "",
    val providerName: String = "",
    val amount: Double = 0.0,
    val status: String = "pending",
    val bookingTime: Timestamp? = null,
    val serviceTime: Timestamp? = null,
    val paymentMethod: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val customerAddress: String = "",
    val imageUrl: String = ""
) : Serializable
