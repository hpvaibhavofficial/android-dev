package com.example.activity_chapter_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Category(
    val name: String,
    val description: String,
    val imageResId: Int
)

class CategoryAdapter(
    private val items: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.categoryImage)
        val nameView: TextView = itemView.findViewById(R.id.categoryName)
        val descView: TextView = itemView.findViewById(R.id.categoryDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.imageView.setImageResource(item.imageResId)
        holder.nameView.text = item.name
        holder.descView.text = item.description

        // ✅ set click listener on the root itemView
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
