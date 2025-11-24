package com.example.helpinghands.ui.admin.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpinghands.data.model.ServiceRequest
import com.example.helpinghands.databinding.FragmentRequestListBinding // <<< FIXED BINDING CLASS NAME
import com.example.helpinghands.ui.customer.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment to display a list of pending service requests for the Admin to dispatch.
 */
@AndroidEntryPoint
class RequestListFragment : Fragment() {

    // --- CRITICAL FIX: Using the correct generated binding class ---
    private var _binding: FragmentRequestListBinding? = null
    private val binding get() = _binding!!

    // Reuse the ServiceViewModel since it contains the logic for fetching and updating requests
    private val serviceViewModel: ServiceViewModel by viewModels()
    private lateinit var requestAdapter: RequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // --- CRITICAL FIX: Inflating the layout using the correct binding class ---
        _binding = FragmentRequestListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observePendingRequests()
    }

    private fun setupRecyclerView() {
        requestAdapter = RequestAdapter(emptyList()) { request ->
            // The adapter handles the action when the Admin clicks "Assign Plumber"
            updateRequest(request)
        }

        binding.rvRequests.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = requestAdapter
        }
    }

    private fun updateRequest(request: ServiceRequest) {
        // Call ViewModel to update status and assign the plumber
        serviceViewModel.updateRequestStatus(
            requestId = request.id,
            newStatus = "PENDING_PLUMBER", // New status
            plumberId = "P101" // Mock Plumber ID
        )
        Toast.makeText(context, "Request ${request.id.take(6)} assigned to P101!", Toast.LENGTH_SHORT).show()
    }

    private fun observePendingRequests() {
        viewLifecycleOwner.lifecycleScope.launch {
            serviceViewModel.adminRequests.collectLatest { result ->
                result.onSuccess { requests ->
                    requestAdapter.updateList(requests)
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.visibility = if (requests.isEmpty()) View.VISIBLE else View.GONE
                }.onFailure {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.text = "Error loading requests: ${it.message}"
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}