package com.example.petcare.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.models.Pet

class FavoritesAdapter(
    private val favList: ArrayList<Pet>,
    private val onRemoveClick: (Pet) -> Unit,
    private val onItemClick: (Pet) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavViewHolder>() {

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFavPet: ImageView = itemView.findViewById(R.id.imgFavPet)
        val tvFavPetName: TextView = itemView.findViewById(R.id.tvFavPetName)
        val tvFavPetInfo: TextView = itemView.findViewById(R.id.tvFavPetInfo)
        val btnRemoveFav: TextView = itemView.findViewById(R.id.btnRemoveFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val pet = favList[position]

        holder.tvFavPetName.text = pet.name
        holder.tvFavPetInfo.text = "${pet.breed} • ${pet.type}"

        Glide.with(holder.itemView.context)
            .load(pet.imageUrl)
            .placeholder(R.drawable.pett)
            .into(holder.imgFavPet)

        holder.btnRemoveFav.setOnClickListener {
            onRemoveClick(pet)
        }

        holder.itemView.setOnClickListener {
            onItemClick(pet)
        }
    }

    override fun getItemCount(): Int = favList.size
}
