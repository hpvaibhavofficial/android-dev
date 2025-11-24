package com.example.helpinghands.repository.data.repo

import com.example.helpinghands.data.model.Service
import com.example.helpinghands.data.model.ServiceRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ServiceRepository {

    private val servicesCollection = firestore.collection("services")
    private val requestsCollection = firestore.collection("service_requests")

    // --- Service Catalog (Customer View) ---

    override fun getAllServiceTypes(): Flow<Result<List<Service>>> = callbackFlow {
        val listenerRegistration = servicesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                return@addSnapshotListener
            }

            val services = snapshot?.toObjects(Service::class.java) ?: emptyList()
            trySend(Result.success(services))
        }

        awaitClose { listenerRegistration.remove() }
    }

    // ⬇️ NEW: ADMIN CRUD IMPLEMENTATIONS ⬇️

    override suspend fun saveServiceType(service: Service): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            // If the ID is empty, create a new document; otherwise, update the existing one.
            val docRef = if (service.id.isEmpty()) servicesCollection.document() else servicesCollection.document(service.id)

            // Set the document, ensuring the model's ID matches the document ID for updates/new creates
            docRef.set(service.copy(id = docRef.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteServiceType(serviceId: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            servicesCollection.document(serviceId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ⬆️ END NEW ADMIN CRUD IMPLEMENTATIONS ⬆️

    // --- Customer Request Operations (EXISTING CODE) ---

    override suspend fun submitServiceRequest(request: ServiceRequest): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val newDocRef = requestsCollection.document()
            val newRequest = request.copy(id = newDocRef.id, status = "NEW")
            newDocRef.set(newRequest).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCustomerServiceHistory(customerId: String): Flow<Result<List<ServiceRequest>>> = callbackFlow {
        if (customerId.isEmpty()) {
            trySend(Result.failure(Exception("Customer ID is missing.")))
            return@callbackFlow
        }

        val listenerRegistration = requestsCollection
            .whereEqualTo("customerId", customerId)
            .orderBy("submittedTimestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                val history = snapshot?.toObjects(ServiceRequest::class.java) ?: emptyList()
                trySend(Result.success(history))
            }

        awaitClose { listenerRegistration.remove() }
    }
    override fun getAllServices(): Flow<Result<List<Service>>> = callbackFlow {
        val listenerRegistration = servicesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                return@addSnapshotListener
            }

            val services = snapshot?.toObjects(Service::class.java) ?: emptyList()
            trySend(Result.success(services))
        }

        awaitClose { listenerRegistration.remove() }
    }


    // --- Admin/Plumber Management Operations (EXISTING CODE) ---

    override fun getAllPendingRequests(): Flow<Result<List<ServiceRequest>>> = callbackFlow {
        val listenerRegistration = requestsCollection
            .whereIn("status", listOf("NEW", "PENDING_PLUMBER"))
            .orderBy("submittedTimestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                val requests = snapshot?.toObjects(ServiceRequest::class.java) ?: emptyList()
                trySend(Result.success(requests))
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun updateRequestStatus(requestId: String, newStatus: String, plumberId: String?): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val updates = mutableMapOf<String, Any>(
                "status" to newStatus
            )
            if (plumberId != null) {
                updates["assignedPlumberId"] = plumberId
            }

            requestsCollection.document(requestId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}