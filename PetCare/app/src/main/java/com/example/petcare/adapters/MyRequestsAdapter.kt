package com.example.petcare.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.models.RequestUIModel

class MyRequestsAdapter(
    private val list: MutableList<RequestUIModel>,
    private val onItemClick: (RequestUIModel) -> Unit
) : RecyclerView.Adapter<MyRequestsAdapter.ReqVH>() {

    class ReqVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgReqPet: ImageView = itemView.findViewById(R.id.imgReqPet)
        val tvReqPetName: TextView = itemView.findViewById(R.id.tvReqPetName)
        val tvReqDate: TextView = itemView.findViewById(R.id.tvReqDate)
        val tvReqStatus: TextView = itemView.findViewById(R.id.tvReqStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReqVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return ReqVH(view)
    }

    override fun onBindViewHolder(holder: ReqVH, position: Int) {
        val item = list[position]

        holder.tvReqPetName.text = item.petName
        holder.tvReqDate.text = "Requested: ${DateFormat.format("dd MMM yyyy", item.createdAt)}"

        val statusLower = item.status.lowercase()
        holder.tvReqStatus.text = statusLower.replaceFirstChar { it.uppercase() }

        when (statusLower) {
            "approved" -> holder.tvReqStatus.setBackgroundResource(R.drawable.bg_status_approved)
            "rejected" -> holder.tvReqStatus.setBackgroundResource(R.drawable.bg_status_rejected)
            else -> holder.tvReqStatus.setBackgroundResource(R.drawable.bg_status_pending)
        }

        Glide.with(holder.itemView.context)
            .load(item.petImageUrl)
            .placeholder(R.drawable.pett)
            .into(holder.imgReqPet)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = list.size
}
