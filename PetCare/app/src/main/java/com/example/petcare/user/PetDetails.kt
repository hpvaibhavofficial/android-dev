package com.example.petcare.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.models.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PetDetails : AppCompatActivity() {

    private lateinit var imgPetDetail: ImageView
    private lateinit var imgFavorite: ImageView

    private lateinit var tvStatusBadge: TextView
    private lateinit var tvPetNameDetail: TextView
    private lateinit var tvBreedTypeDetail: TextView
    private lateinit var tvAgeDetail: TextView
    private lateinit var tvGenderDetail: TextView
    private lateinit var tvPriceDetail: TextView
    private lateinit var tvTagsDetail: TextView

    private lateinit var btnBookVisit: AppCompatButton
    private lateinit var btnAdoptNow: AppCompatButton

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    private var currentPet: Pet? = null
    private var petId: String? = null

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pet_details)

        petId = intent.getStringExtra("petId")
        if (petId.isNullOrEmpty()) {
            Toast.makeText(this, "Pet not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        bindViews()
        fetchPetDetails()

        imgFavorite.setOnClickListener {
            toggleFavorite()
        }

        btnAdoptNow.setOnClickListener {
            sendAdoptionRequest()
        }

        btnBookVisit.setOnClickListener {
            openBookVisit()
        }
    }

    private fun bindViews() {
        imgPetDetail = findViewById(R.id.imgPetDetail)
        imgFavorite = findViewById(R.id.imgFavorite)

        tvStatusBadge = findViewById(R.id.tvStatusBadge)
        tvPetNameDetail = findViewById(R.id.tvPetNameDetail)
        tvBreedTypeDetail = findViewById(R.id.tvBreedTypeDetail)
        tvAgeDetail = findViewById(R.id.tvAgeDetail)
        tvGenderDetail = findViewById(R.id.tvGenderDetail)
        tvPriceDetail = findViewById(R.id.tvPriceDetail)
        tvTagsDetail = findViewById(R.id.tvTagsDetail)

        btnBookVisit = findViewById(R.id.btnBookVisit)
        btnAdoptNow = findViewById(R.id.btnAdoptNow)
    }

    private fun fetchPetDetails() {
        firestore.collection("pets")
            .document(petId!!)
            .get()
            .addOnSuccessListener { doc ->
                val pet = doc.toObject(Pet::class.java)
                if (pet == null) {
                    Toast.makeText(this, "Pet not found", Toast.LENGTH_SHORT).show()
                    finish()
                    return@addOnSuccessListener
                }

                currentPet = pet
                showPet(pet)

                // ✅ Run these AFTER pet loads
                checkFavorite()
                checkIfAlreadyRequested()
            }
            .addOnFailureListener { e ->
                Log.e("PET_DETAILS", "Error", e)
                Toast.makeText(this, "Failed to load pet", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showPet(pet: Pet) {
        tvStatusBadge.text = pet.status
        tvPetNameDetail.text = pet.name
        tvBreedTypeDetail.text = "${pet.breed} • ${pet.type}"
        tvAgeDetail.text = "Age: ${pet.age}"
        tvGenderDetail.text = "Gender: ${pet.gender}"
        tvPriceDetail.text = "₹ ${pet.price}"
        tvTagsDetail.text =
            if (pet.tags.isNotEmpty()) "Tags: ${pet.tags.joinToString(", ")}" else "Tags: -"

        Glide.with(this)
            .load(pet.imageUrl)
            .placeholder(R.drawable.pett)
            .into(imgPetDetail)
    }

    // ✅ FAVORITE CHECK
    private fun checkFavorite() {
        val user = auth.currentUser ?: return
        val pet = currentPet ?: return

        firestore.collection("users")
            .document(user.uid)
            .collection("favorites")
            .document(pet.petId)
            .get()
            .addOnSuccessListener { doc ->
                isFavorite = doc.exists()
                updateFavIcon()
            }
    }

    // ✅ FAVORITE TOGGLE
    private fun toggleFavorite() {
        val user = auth.currentUser
        val pet = currentPet

        if (user == null || pet == null) {
            Toast.makeText(this, "Login required", Toast.LENGTH_SHORT).show()
            return
        }

        val favRef = firestore.collection("users")
            .document(user.uid)
            .collection("favorites")
            .document(pet.petId)

        if (isFavorite) {
            favRef.delete()
                .addOnSuccessListener {
                    isFavorite = false
                    updateFavIcon()
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                }
        } else {
            val favData = hashMapOf(
                "petId" to pet.petId,
                "addedAt" to System.currentTimeMillis()
            )

            favRef.set(favData)
                .addOnSuccessListener {
                    isFavorite = true
                    updateFavIcon()
                    Toast.makeText(this, "Added to favorites ❤️", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateFavIcon() {
        if (isFavorite) {
            imgFavorite.setImageResource(R.drawable.ic_heart_filled)
        } else {
            imgFavorite.setImageResource(R.drawable.ic_heart_outline)
        }
    }

    // ✅ ONE REQUEST PER PET PER USER + BUTTON UPDATE
    private fun sendAdoptionRequest() {
        val user = auth.currentUser
        val pet = currentPet

        if (user == null || pet == null) {
            Toast.makeText(this, "Login required", Toast.LENGTH_SHORT).show()
            return
        }

        val requestId = "${user.uid}_${pet.petId}"
        val requestRef = firestore.collection("adoptionRequests").document(requestId)

        requestRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                Toast.makeText(this, "You already requested this pet ✅", Toast.LENGTH_LONG).show()
                btnAdoptNow.text = "Requested ✅"
                btnAdoptNow.isEnabled = false
                btnAdoptNow.alpha = 0.7f
                return@addOnSuccessListener
            }

            val requestData = hashMapOf(
                "requestId" to requestId,
                "petId" to pet.petId,
                "userId" to user.uid,
                "status" to "pending",
                "createdAt" to System.currentTimeMillis()
            )

            requestRef.set(requestData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Adoption request sent ✅", Toast.LENGTH_SHORT).show()

                    // ✅ Update button instantly
                    btnAdoptNow.text = "Requested ✅"
                    btnAdoptNow.isEnabled = false
                    btnAdoptNow.alpha = 0.7f
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Request failed ❌", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // ✅ BOOK VISIT
    private fun openBookVisit() {
        val pet = currentPet
        if (pet == null) {
            Toast.makeText(this, "Pet not loaded yet", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, BookVisit::class.java)
        intent.putExtra("petId", pet.petId)
        intent.putExtra("petName", pet.name)
        startActivity(intent)
    }

    // ✅ CHECK REQUEST ON SCREEN OPEN
    private fun checkIfAlreadyRequested() {
        val user = auth.currentUser ?: return
        val pet = currentPet ?: return

        val requestId = "${user.uid}_${pet.petId}"

        firestore.collection("adoptionRequests")
            .document(requestId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    btnAdoptNow.text = "Requested ✅"
                    btnAdoptNow.isEnabled = false
                    btnAdoptNow.alpha = 0.7f
                } else {
                    btnAdoptNow.text = "Adopt Now"
                    btnAdoptNow.isEnabled = true
                    btnAdoptNow.alpha = 1f
                }
            }
    }
}
