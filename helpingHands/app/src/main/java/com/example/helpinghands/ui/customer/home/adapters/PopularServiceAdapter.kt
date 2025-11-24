package com.example.helpinghands.ui.customer.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpinghands.R
import com.example.helpinghands.data.model.Service
import com.example.helpinghands.databinding.ItemPopularServiceBinding

class PopularServiceAdapter(
    private var items: List<Service> = emptyList(),
    private val onItemClick: (Service) -> Unit = {}
) : RecyclerView.Adapter<PopularServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(val binding: ItemPopularServiceBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemPopularServiceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = items[position]

        holder.binding.apply {
            tvServiceName.text = service.name
            tvServiceDesc.text = service.description

            // Load image
            if (!service.imageUrl.isNullOrEmpty()) {
                Glide.with(holder.itemView)
                    .load(service.imageUrl)
                    .into(ivServiceImage)
            } else {
                ivServiceImage.setImageResource(R.drawable.ic_service_placeholder)
            }

            root.setOnClickListener {
                onItemClick(service)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<Service>) {
        items = newList
        notifyDataSetChanged()
    }
}
