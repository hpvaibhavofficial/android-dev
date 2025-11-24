package com.example.helpinghands.ui.customer.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghands.data.model.Product
import com.example.helpinghands.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product) -> Unit // Click listener for Add button
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = "$${String.format("%.2f", product.price)}"
            binding.tvProductCategory.text = "Category: ${product.categoryId}"
            binding.tvStock.text = "Stock: ${product.stockQuantity}"

            // Set up Add to Cart listener
            binding.btnAdd.setOnClickListener {
                onAddToCart(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    // Function to update the list when the Flow emits new data
    fun updateList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}