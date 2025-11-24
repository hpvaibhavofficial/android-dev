package com.example.helpinghands.data.model

data class ServiceRequest(
    val id: String = "",
    val customerId: String = "", // Used in getCustomerServiceHistory
    val customerName: String = "",
    val serviceId: String = "",
    val serviceName: String = "",
    val issueDescription: String = "",
    val preferredDate: Long = 0L, // Timestamp for preferred time
    val address: String = "",
    val status: String = "NEW", // NEW, PENDING_PLUMBER, IN_PROGRESS, COMPLETED, CANCELED
    val assignedPlumberId: String? = null,
    val submittedTimestamp: Long = System.currentTimeMillis() // Used for ordering (orderBy)
)