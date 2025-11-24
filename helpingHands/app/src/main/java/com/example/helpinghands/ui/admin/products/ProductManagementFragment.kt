package com.example.helpinghands.ui.admin.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpinghands.data.model.Product
// CRITICAL FIX: Changing the binding import name
import com.example.helpinghands.databinding.FragmentProductManagementBinding
import com.example.helpinghands.ui.customer.ECommerceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductManagementFragment : Fragment() {

    // CRITICAL FIX: Using the correct generated binding class name
    private var _binding: FragmentProductManagementBinding? = null
    private val binding get() = _binding!!

    private val eCommerceViewModel: ECommerceViewModel by viewModels()
    private lateinit var productAdminAdapter: ProductAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // CRITICAL FIX: Inflating the correct binding
        _binding = FragmentProductManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeProducts()

        binding.fabAddProduct.setOnClickListener {
            showProductDialog(null)
        }
    }

    private fun setupRecyclerView() {
        productAdminAdapter = ProductAdminAdapter(
            emptyList(),
            onEdit = { product -> showProductDialog(product) },
            onDelete = { product -> confirmDelete(product) }
        )
        binding.rvAdminProducts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdminAdapter
        }
    }

    private fun observeProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            eCommerceViewModel.products.collectLatest { result ->
                result.onSuccess { products ->
                    productAdminAdapter.updateList(products)
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
                }.onFailure {
                    binding.progressBar.visibility = View.GONE
                    binding.tvEmpty.text = "Error loading catalog: ${it.message}"
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showProductDialog(product: Product?) {
        if (product == null) {
            Toast.makeText(context, "Opening dialog to ADD new product...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Opening dialog to EDIT product: ${product.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDelete(product: Product) {
        eCommerceViewModel.deleteProduct(product.id)
        Toast.makeText(context, "Deleted product: ${product.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}