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
import com.example.petcare.adapters.MyRequestsAdapter
import com.example.petcare.models.AdoptionRequest
import com.example.petcare.models.Pet
import com.example.petcare.models.RequestUIModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyRequests : AppCompatActivity() {
    private lateinit var rvRequests: RecyclerView
    private lateinit var adapter: MyRequestsAdapter
    private val uiList = mutableListOf<RequestUIModel>()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_requests)

        rvRequests = findViewById(R.id.rvRequests)

        adapter = MyRequestsAdapter(uiList) { clickedItem ->
            val intent = Intent(this, PetDetails::class.java)
            intent.putExtra("petId", clickedItem.petId)
            startActivity(intent)
        }

        rvRequests.layoutManager = LinearLayoutManager(this)
        rvRequests.adapter = adapter

        loadMyRequests()
    }

    private fun loadMyRequests() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Login required", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        firestore.collection("adoptionRequests")
            .whereEqualTo("userId", user.uid)
            .get()
            .addOnSuccessListener { reqSnapshot ->

                uiList.clear()

                if (reqSnapshot.isEmpty) {
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "No requests yet ✅", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val requests = reqSnapshot.toObjects(AdoptionRequest::class.java)
                    .sortedByDescending { it.createdAt }   // ✅ Latest first

                requests.forEach { req ->
                    firestore.collection("pets")
                        .document(req.petId)
                        .get()
                        .addOnSuccessListener { petDoc ->
                            val pet = petDoc.toObject(Pet::class.java)

                            if (pet != null) {
                                uiList.add(
                                    RequestUIModel(
                                        requestId = req.requestId,
                                        petId = pet.petId,
                                        petName = pet.name,
                                        petImageUrl = pet.imageUrl,
                                        status = req.status,
                                        createdAt = req.createdAt
                                    )
                                )
                                adapter.notifyDataSetChanged()
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("MY_REQUESTS", "Pet fetch failed", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("MY_REQUESTS", "Failed", e)
                Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show()
            }
    }
}
