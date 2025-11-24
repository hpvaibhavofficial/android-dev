package com.example.helpinghands.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpinghands.data.model.Service
import com.example.helpinghands.data.model.ServiceRequest
import com.example.helpinghands.repository.data.repo.ServiceRepository // Corrected package import for consistency
import com.example.helpinghands.repository.data.repo.AuthRepository // ✅ NEW: Injecting AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the Service Booking process (Customer) and viewing Requests (Admin).
 */
@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val authRepository: AuthRepository // ✅ FIX: Injecting AuthRepository instead of AuthViewModel
) : ViewModel() {

    // ✅ FIX: Get customerId directly from the injected Repository
    private val customerId: String? = authRepository.currentUserId

    // --- CUSTOMER STATES ---
    private val _availableServices = MutableStateFlow<Result<List<Service>>>(Result.success(emptyList()))
    val availableServices: StateFlow<Result<List<Service>>> = _availableServices

    private val _submissionStatus = MutableStateFlow<Result<Unit>?>(null)
    val submissionStatus: StateFlow<Result<Unit>?> = _submissionStatus

    // --- ADMIN REQUESTS STATE ---
    val adminRequests: StateFlow<Result<List<ServiceRequest>>> =
        serviceRepository.getAllPendingRequests()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.success(emptyList())
            )

    init {
        fetchAvailableServices()
    }

    private fun fetchAvailableServices() {
        serviceRepository.getAllServiceTypes()
            .onEach { result ->
                _availableServices.value = result
            }
            .launchIn(viewModelScope)
    }

    /**
     * Submits a new service request to the repository (Customer action).
     */
    fun submitRequest(
        service: Service,
        description: String,
        address: String,
        preferredDate: Long
    ) {
        val id = customerId
        if (id == null) {
            _submissionStatus.value = Result.failure(Exception("Authentication required to submit request."))
            return
        }

        _submissionStatus.value = Result.failure(Exception("Loading..."))

        val newRequest = ServiceRequest(
            customerId = id,
            serviceId = service.id,
            serviceName = service.name,
            issueDescription = description,
            preferredDate = preferredDate,
            address = address
        )

        viewModelScope.launch {
            val result = serviceRepository.submitServiceRequest(newRequest)
            _submissionStatus.value = result
        }
    }

    // --- ADMIN MANAGEMENT ACTIONS (EXISTING) ---

    /** Allows the Admin to update a request status (e.g., assign a plumber) */
    fun updateRequestStatus(requestId: String, newStatus: String, plumberId: String? = null) {
        viewModelScope.launch {
            serviceRepository.updateRequestStatus(requestId, newStatus, plumberId)
        }
    }

    // --- SERVICE TYPE CRUD ACTIONS (FOR ADMIN UI) ---

    /** Saves a new service type or updates an existing one (Admin action). */
    fun saveServiceType(service: Service) {
        viewModelScope.launch {
            serviceRepository.saveServiceType(service)
        }
    }

    /** Deletes a service type from the catalog (Admin action). */
    fun deleteServiceType(serviceId: String) {
        viewModelScope.launch {
            serviceRepository.deleteServiceType(serviceId)
        }
    }
}