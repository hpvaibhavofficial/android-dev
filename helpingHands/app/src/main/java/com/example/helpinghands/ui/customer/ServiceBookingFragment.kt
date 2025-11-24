package com.example.helpinghands.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.helpinghands.R
import com.example.helpinghands.data.model.Service
import com.example.helpinghands.databinding.FragmentServiceBookingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Fragment where the customer selects a service type and submits a booking request.
 * It observes the ServiceViewModel for available services and submission status.
 */
@AndroidEntryPoint
class ServiceBookingFragment : Fragment() {

    private var _binding: FragmentServiceBookingBinding? = null
    private val binding get() = _binding!!

    private val serviceViewModel: ServiceViewModel by viewModels()
    private var availableServices: List<Service> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start observing data flows
        observeAvailableServices()
        observeSubmissionStatus()

        binding.btnSubmitRequest.setOnClickListener {
            submitBooking()
        }
    }

    private fun observeAvailableServices() {
        viewLifecycleOwner.lifecycleScope.launch {
            serviceViewModel.availableServices.collectLatest { result ->
                result.onSuccess { services ->
                    availableServices = services
                    setupServiceSpinner(services)
                }.onFailure {
                    Toast.makeText(context, "Error loading services. Check Firebase setup.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupServiceSpinner(services: List<Service>) {
        // Map Service objects to strings for display in the Spinner
        val serviceNames = services.map { "${it.name} (Base: $${String.format("%.2f", it.basePrice)})" }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            serviceNames
        )
        binding.spinnerServiceType.adapter = adapter
    }

    private fun submitBooking() {
        val selectedIndex = binding.spinnerServiceType.selectedItemPosition
        if (availableServices.isEmpty() || selectedIndex == -1) {
            Toast.makeText(context, "Please wait for services to load or select a service.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedService = availableServices[selectedIndex]
        val description = binding.etIssueDescription.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val preferredDate = Date().time // Using current timestamp as a placeholder for preferred date/time

        if (description.isEmpty() || address.isEmpty()) {
            Toast.makeText(context, "Please describe the issue and provide an address.", Toast.LENGTH_LONG).show()
            return
        }

        // Call ViewModel to submit the request
        serviceViewModel.submitRequest(
            service = selectedService,
            description = description,
            address = address,
            preferredDate = preferredDate
        )
    }

    private fun observeSubmissionStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            serviceViewModel.submissionStatus.collectLatest { status ->
                when {
                    status == null -> { /* Initial state / No action taken */ }
                    status.isSuccess -> {
                        Toast.makeText(context, "Request submitted successfully! We will contact you soon.", Toast.LENGTH_LONG).show()
                        binding.btnSubmitRequest.isEnabled = true
                        // Clear input fields on success (Optional)
                        binding.etIssueDescription.setText("")
                        // Navigate back to the home screen (Optional)
                    }
                    status.isFailure -> {
                        val message = status.exceptionOrNull()?.message
                        if (message != "Loading...") {
                            Toast.makeText(context, "Submission Failed: ${message}", Toast.LENGTH_LONG).show()
                            binding.btnSubmitRequest.isEnabled = true
                        } else {
                            // Indicate Loading State
                            binding.btnSubmitRequest.isEnabled = false
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}