package com.example.petcare.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.petcare.R
import com.example.petcare.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: AppCompatButton
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Bind views
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        btnRegister.setOnClickListener {
            registerUser()
        }

        tvLogin.setOnClickListener {
            finish() // back to Login
        }
    }

    private fun registerUser() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // ---------- VALIDATIONS ----------
        if (name.isEmpty()) {
            etName.error = "Name required"
            etName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Email required"
            etEmail.requestFocus()
            return
        }

        if (phone.isEmpty()) {
            etPhone.error = "Phone required"
            etPhone.requestFocus()
            return
        }

        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            etPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords do not match"
            etConfirmPassword.requestFocus()
            return
        }

        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show()
            return
        }

        btnRegister.isEnabled = false

        // ---------- FIREBASE AUTH ----------
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user!!.uid

                val user = User(
                    uid = uid,
                    name = name,
                    email = email,
                    phone = phone
                    // role, city use default values
                )

                // ---------- SAVE TO FIRESTORE ----------
                db.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Login::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        btnRegister.isEnabled = true
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                btnRegister.isEnabled = true
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }
}
