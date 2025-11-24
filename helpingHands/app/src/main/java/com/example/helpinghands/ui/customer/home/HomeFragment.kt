package com.example.helpinghands.ui.customer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpinghands.data.model.Product
import com.example.helpinghands.data.model.Service
import com.example.helpinghands.databinding.FragmentHomeBinding
import com.example.helpinghands.ui.customer.home.adapters.FeaturedProductAdapter
import com.example.helpinghands.ui.customer.home.adapters.PopularServiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var featuredAdapter: FeaturedProductAdapter
    private lateinit var serviceAdapter: PopularServiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQuickActions()
        setupRecyclerViews()
        observeData()
    }

    private fun setupQuickActions() {
        binding.cardBookPlumber.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToServiceBookingFragment()
            )
        }
    }

    private fun setupRecyclerViews() {

        // ⭐ Featured Products Adapter (Only onAddClick needed)
        featuredAdapter = FeaturedProductAdapter(
            onAddClick = { product ->
                Toast.makeText(requireContext(), "Added: ${product.name}", Toast.LENGTH_SHORT).show()

            }
        )

        binding.rvFeaturedProducts.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = featuredAdapter
        }

        // ⭐ Popular Services Adapter
        serviceAdapter = PopularServiceAdapter { service: Service ->
            // TODO: Navigate to service detail
        }

        binding.rvPopularServices.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = serviceAdapter
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            // Observe Featured Products
            viewModel.featuredProducts.collectLatest { list ->
                featuredAdapter.submitList(list)
            }

            // Observe Popular Services
            viewModel.popularServices.collectLatest { list ->
                serviceAdapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
