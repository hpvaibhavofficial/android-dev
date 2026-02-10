package com.example.petcare.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.R
import com.example.petcare.adapters.PetAdapter
import com.example.petcare.models.Pet
import com.google.firebase.firestore.FirebaseFirestore

class PetList : AppCompatActivity() {

    private lateinit var rvPets: RecyclerView
    private lateinit var petAdapter: PetAdapter
    private val petList = arrayListOf<Pet>()

    private lateinit var btnFavorites: ImageView
    private lateinit var btnRequests: ImageView

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pet_list)

        rvPets = findViewById(R.id.rvPets)
        btnFavorites = findViewById(R.id.btnFavorites)
        btnRequests = findViewById(R.id.btnRequests)

        // RecyclerView setup
        petAdapter = PetAdapter(petList) { pet ->
            Toast.makeText(this, "Clicked: ${pet.name}", Toast.LENGTH_SHORT).show()

            // ✅ Optional: open PetDetails screen here
            // val intent = Intent(this, PetDetails::class.java)
            // intent.putExtra("petId", pet.petId)
            // startActivity(intent)
        }

        rvPets.layoutManager = LinearLayoutManager(this)
        rvPets.adapter = petAdapter

        // ✅ Favorites click
        btnFavorites.setOnClickListener {
            startActivity(Intent(this, Favourites::class.java))
        }

        // ✅ My Requests click
        btnRequests.setOnClickListener {
            startActivity(Intent(this, MyRequests::class.java))
        }

        fetchPets()
    }

    private fun fetchPets() {
        firestore.collection("pets")
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { snapshot ->
                petList.clear()

                for (doc in snapshot.documents) {
                    val pet = doc.toObject(Pet::class.java)
                    if (pet != null) {
                        petList.add(pet)
                    }
                }

                petAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("PET_LIST", "Error fetching pets", e)
                Toast.makeText(this, "Failed to load pets", Toast.LENGTH_LONG).show()
            }
    }
}
