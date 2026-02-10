package com.example.petcare.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.models.Pet
import com.example.petcare.user.PetDetails

class PetAdapter(
    private val petList: ArrayList<Pet>,
    private val onItemClick: (Pet) -> Unit
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPet: ImageView = itemView.findViewById(R.id.imgPet)
        val tvPetName: TextView = itemView.findViewById(R.id.tvPetName)
        val tvBreedType: TextView = itemView.findViewById(R.id.tvBreedType)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petList[position]



        holder.tvPetName.text = pet.name
        holder.tvBreedType.text = "${pet.breed} • ${pet.type}"
        holder.tvPrice.text = "₹ ${pet.price}"
        holder.tvStatus.text = pet.status

        Glide.with(holder.itemView.context)
            .load(pet.imageUrl)
            .placeholder(R.drawable.pett)
            .into(holder.imgPet)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PetDetails::class.java)
            intent.putExtra("petId", pet.petId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = petList.size
}
