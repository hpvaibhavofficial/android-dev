package com.example.helpinghands.ui.admin.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpinghands.data.model.Service
import com.example.helpinghands.databinding.FragmentAdminServicesBinding
import com.example.helpinghands.ui.customer.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment for the Admin to manage the list of available Service Types (CRUD).
 */
@AndroidEntryPoint
class ServiceManagementFragment : Fragment() {

    private var _binding: FragmentAdminServicesBinding? = null
    private val binding get() = _binding!!

    private val serviceViewModel: ServiceViewModel by viewModels()
    private lateinit var serviceAdminAdapter: ServiceAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeServiceTypes()

        binding.fabAddService.setOnClickListener {
            showServiceDialog(null) // Open dialog for new service type creation
        }
    }

    private fun setupRecyclerView() {
        serviceAdminAdapter = ServiceAdminAdapter(
            emptyList(),
            onEdit = { service -> showServiceDialog(service) },
            onDelete = { service -> confirmDelete(service) }
        )
        binding.rvAdminServices.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = serviceAdminAdapter
        }
    }

    private fun observeServiceTypes() {
        viewLifecycleOwner.lifecycleScope.launch {
            serviceViewModel.availableServices.collectLatest { result ->
                result.onSuccess { services ->
                    serviceAdminAdapter.updateList(services)
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.visibility = if (services.isEmpty()) View.VISIBLE else View.GONE
                }.onFailure {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.text = "Error loading services: ${it.message}"
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showServiceDialog(service: Service?) {
        if (service == null) {
            Toast.makeText(context, "Opening dialog to ADD new service type...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Opening dialog to EDIT service: ${service.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDelete(service: Service) {
        serviceViewModel.deleteServiceType(service.id)
        Toast.makeText(context, "Deleted service: ${service.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}