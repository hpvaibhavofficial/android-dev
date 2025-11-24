package com.example.helpinghands.ui.admin.requests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghands.data.model.ServiceRequest
import com.example.helpinghands.databinding.ItemServiceRequestBinding

class RequestAdapter(
    private var requests: List<ServiceRequest>,
    private val onAssignClicked: (ServiceRequest) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(private val binding: ItemServiceRequestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(request: ServiceRequest) {
            binding.tvRequestId.text = "ID: ${request.id.take(6)}" // Show short ID
            binding.tvServiceType.text = "Service: ${request.serviceName}"
            binding.tvIssueDescription.text = "Issue: ${request.issueDescription}"
            binding.tvAddress.text = "Address: ${request.address}"
            binding.tvStatus.text = "Status: ${request.status}"

            // Highlight new/pending status (using hardcoded colors for simplicity)
            val color = when (request.status) {
                "NEW" -> 0xFFFF9800.toInt() // Orange
                "PENDING_PLUMBER" -> 0xFF2196F3.toInt() // Blue
                else -> 0xFF607D8B.toInt() // Gray
            }
            binding.tvStatus.setTextColor(color)

            binding.btnAssign.setOnClickListener {
                onAssignClicked(request)
            }

            // Hide assign button if already assigned/in progress
            binding.btnAssign.visibility = if (request.assignedPlumberId == null) View.VISIBLE else View.GONE
            binding.btnAssign.text = if (request.assignedPlumberId != null) "Assigned" else "Assign Plumber"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemServiceRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(requests[position])
    }

    override fun getItemCount(): Int = requests.size

    fun updateList(newRequests: List<ServiceRequest>) {
        requests = newRequests
        notifyDataSetChanged()
    }
}