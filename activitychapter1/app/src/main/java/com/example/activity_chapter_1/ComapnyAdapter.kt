package com.example.activity_chapter_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CompanyAdapter(
    private val companies: List<Company>
) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView as CardView
        val imageView: ImageView = itemView.findViewById(R.id.companyImage)
        val nameView: TextView = itemView.findViewById(R.id.companyName)
        val infoView: TextView = itemView.findViewById(R.id.companyInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.company_item, parent, false)
        return CompanyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = companies[position]
        holder.imageView.setImageResource(company.imageResId)
        holder.nameView.text = company.name
        holder.infoView.text = company.info
    }

    override fun getItemCount(): Int = companies.size
}
