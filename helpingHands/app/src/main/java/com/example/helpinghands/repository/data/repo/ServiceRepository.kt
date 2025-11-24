package com.example.helpinghands.repository.data.repo

import com.example.helpinghands.data.model.Service
import com.example.helpinghands.data.model.ServiceRequest
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing Service Catalogs and Service Requests (bookings).
 */
interface ServiceRepository {

    fun getAllServiceTypes(): Flow<Result<List<Service>>>

    fun getAllServices(): Flow<Result<List<Service>>>   // 🔥 NEW FOR HOME PAGE

    suspend fun saveServiceType(service: Service): Result<Unit>

    suspend fun deleteServiceType(serviceId: String): Result<Unit>

    suspend fun submitServiceRequest(request: ServiceRequest): Result<Unit>

    fun getCustomerServiceHistory(customerId: String): Flow<Result<List<ServiceRequest>>>

    fun getAllPendingRequests(): Flow<Result<List<ServiceRequest>>>

    suspend fun updateRequestStatus(requestId: String, newStatus: String, plumberId: String?): Result<Unit>
}
