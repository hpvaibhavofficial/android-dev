package com.example.helpinghands.ui.customer.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghands.data.model.CartItem
import com.example.helpinghands.databinding.ItemCartBinding

class CardAdapter(
    private var cartItems: List<CartItem>,
    private val onUpdateQuantity: (String, Int) -> Unit // (productId, newQuantity)
) : RecyclerView.Adapter<CardAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.tvCartItemName.text = item.name
            binding.tvCartItemPrice.text = "$${String.format("%.2f", item.price * item.quantity)}"
            binding.tvQuantity.text = item.quantity.toString()

            // Decrement button listener
            binding.btnDecrement.setOnClickListener {
                if (item.quantity > 0) {
                    onUpdateQuantity(item.productId, item.quantity - 1)
                }
            }

            // Increment button listener
            binding.btnIncrement.setOnClickListener {
                onUpdateQuantity(item.productId, item.quantity + 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateList(newItems: List<CartItem>) {
        cartItems = newItems
        notifyDataSetChanged()
    }
}