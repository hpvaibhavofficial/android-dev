package com.example.cardview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private val items : List<CardItem>,
    private val onitemClick : (CardItem) -> Unit
) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardView : CardView = itemView as CardView
        val imageView : ImageView = itemView.findViewById(R.id.cardImage)
        val titleView : TextView = itemView.findViewById(R.id.cardTitle)
        val subtitleView : TextView = itemView.findViewById(R.id.cardSubtitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.img)
        holder.titleView.text =  item.title
        holder.subtitleView.text = item.subtitle

        holder.cardView.setOnClickListener(){
            onitemClick(item)
        }
    }
}