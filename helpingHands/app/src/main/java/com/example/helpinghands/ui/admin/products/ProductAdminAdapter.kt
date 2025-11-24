package com.example.helpinghands.ui.admin.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghands.data.model.Product
import com.example.helpinghands.databinding.ItemAdminProductBinding // NEW layout file needed!

class ProductAdminAdapter(
    private var products: List<Product>,
    private val onEdit: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdminAdapter.ProductAdminViewHolder>() {

    inner class ProductAdminViewHolder(private val binding: ItemAdminProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = "$${String.format("%.2f", product.price)}"
            binding.tvProductDetails.text = "Stock: ${product.stockQuantity} | Cat: ${product.categoryId}"

            // Set up Admin action listeners
            binding.btnEdit.setOnClickListener { onEdit(product) }
            binding.btnDelete.setOnClickListener { onDelete(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdminViewHolder {
        val binding = ItemAdminProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductAdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdminViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}