package com.example.petcare.user

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.petcare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookVisit : AppCompatActivity() {

    private lateinit var tvPetName: TextView
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var etPhone: EditText
    private lateinit var etNote: EditText
    private lateinit var btnSubmitBooking: AppCompatButton

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    private var petId: String? = null
    private var petName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_visit)

        petId = intent.getStringExtra("petId")
        petName = intent.getStringExtra("petName")

        if (petId.isNullOrEmpty()) {
            Toast.makeText(this, "Pet not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        bindViews()

        tvPetName.text = "Pet: ${petName ?: "-"}"

        btnSubmitBooking.setOnClickListener {
            submitAppointment()
        }
    }

    private fun bindViews() {
        tvPetName = findViewById(R.id.tvPetName)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        etPhone = findViewById(R.id.etPhone)
        etNote = findViewById(R.id.etNote)
        btnSubmitBooking = findViewById(R.id.btnSubmitBooking)
    }

    private fun submitAppointment() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Login required", Toast.LENGTH_SHORT).show()
            return
        }

        val date = etDate.text.toString().trim()
        val time = etTime.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val note = etNote.text.toString().trim()

        if (date.isEmpty()) {
            etDate.error = "Date required"
            etDate.requestFocus()
            return
        }

        if (time.isEmpty()) {
            etTime.error = "Time required"
            etTime.requestFocus()
            return
        }

        if (phone.isEmpty()) {
            etPhone.error = "Phone required"
            etPhone.requestFocus()
            return
        }

        btnSubmitBooking.isEnabled = false

        val appointmentRef = firestore.collection("appointments").document()

        val data = hashMapOf(
            "appointmentId" to appointmentRef.id,
            "petId" to petId,
            "petName" to (petName ?: ""),
            "userId" to user.uid,
            "date" to date,
            "time" to time,
            "phone" to phone,
            "note" to note,
            "status" to "pending",
            "createdAt" to System.currentTimeMillis()
        )

        appointmentRef.set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Appointment request sent ✅", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { e ->
                btnSubmitBooking.isEnabled = true
                Log.e("BOOK_VISIT", "Failed", e)
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
