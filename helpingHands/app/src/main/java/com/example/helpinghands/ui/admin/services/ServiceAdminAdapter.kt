package com.example.helpinghands.ui.admin.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghands.data.model.Service
import com.example.helpinghands.databinding.ItemAdminServiceBinding // NEW layout needed!

class ServiceAdminAdapter(
    private var services: List<Service>,
    private val onEdit: (Service) -> Unit,
    private val onDelete: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdminAdapter.ServiceAdminViewHolder>() {

    inner class ServiceAdminViewHolder(private val binding: ItemAdminServiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(service: Service) {
            binding.tvServiceName.text = service.name
            binding.tvServicePrice.text = "$${String.format("%.2f", service.basePrice)}"
            binding.tvServiceDetails.text = "Duration: ${service.estimatedDurationHours} hrs"

            binding.btnEdit.setOnClickListener { onEdit(service) }
            binding.btnDelete.setOnClickListener { onDelete(service) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceAdminViewHolder {
        val binding = ItemAdminServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceAdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceAdminViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount(): Int = services.size

    fun updateList(newServices: List<Service>) {
        services = newServices
        notifyDataSetChanged()
    }
}