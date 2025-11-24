package com.example.helpinghands.ui.customer.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpinghands.R
import com.example.helpinghands.data.model.Product
import com.example.helpinghands.databinding.ItemFeaturedProductBinding

class FeaturedProductAdapter(
    private val onAddClick: (Product) -> Unit,
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, FeaturedProductAdapter.ProductViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    inner class ProductViewHolder(val binding: ItemFeaturedProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemFeaturedProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)

        holder.binding.apply {

            tvProductName.text = product.name
            tvProductPrice.text = "₹${product.price}"

            // Load product image
            Glide.with(ivProductImage.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.pipe)
                .error(R.drawable.pipe)
                .into(ivProductImage)

            // Add to Cart button click
            btnAdd.setOnClickListener {
                onAddClick(product)
            }

            // Entire card click → Product Details
            root.setOnClickListener {
                onItemClick(product)
            }
        }
    }
}
