package com.example.petcare.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.adapters.FavoritesAdapter
import com.example.petcare.models.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Favourites : AppCompatActivity() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var adapter: FavoritesAdapter
    private val favList = arrayListOf<Pet>()

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favourites)

        rvFavorites = findViewById(R.id.rvFavorites)

        adapter = FavoritesAdapter(
            favList,
            onRemoveClick = { pet -> removeFromFavorites(pet) },
            onItemClick = { pet ->
                val intent = Intent(this, PetDetails::class.java)
                intent.putExtra("petId", pet.petId)
                startActivity(intent)
            }
        )

        rvFavorites.layoutManager = LinearLayoutManager(this)
        rvFavorites.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Login required", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        firestore.collection("users")
            .document(user.uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { favSnapshot ->

                if (favSnapshot.isEmpty) {
                    favList.clear()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "No favorites yet ❤️", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val petIds = favSnapshot.documents.mapNotNull { it.getString("petId") }

                if (petIds.isEmpty()) {
                    favList.clear()
                    adapter.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                // Firestore whereIn max 10 items, so split if needed
                favList.clear()

                val chunks = petIds.chunked(10)

                for (chunk in chunks) {
                    firestore.collection("pets")
                        .whereIn("petId", chunk)
                        .get()
                        .addOnSuccessListener { petsSnapshot ->
                            for (doc in petsSnapshot.documents) {
                                val pet = doc.toObject(Pet::class.java)
                                if (pet != null) favList.add(pet)
                            }
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { e ->
                            Log.e("FAVORITES", "Failed to load pets", e)
                            Toast.makeText(this, "Failed to load favorites", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FAVORITES", "Failed to load favorites list", e)
                Toast.makeText(this, "Error loading favorites", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromFavorites(pet: Pet) {
        val user = auth.currentUser ?: return

        firestore.collection("users")
            .document(user.uid)
            .collection("favorites")
            .document(pet.petId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Removed ✅", Toast.LENGTH_SHORT).show()
                loadFavorites()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Remove failed ❌", Toast.LENGTH_SHORT).show()
            }
    }
}
