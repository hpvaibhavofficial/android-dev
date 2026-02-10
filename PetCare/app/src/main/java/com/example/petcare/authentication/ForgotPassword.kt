package com.example.petcare.authentication

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.petcare.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var btnReset: AppCompatButton
    private lateinit var tvBackToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        btnReset = findViewById(R.id.btnReset)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)

        btnReset.setOnClickListener {
            sendResetEmail()
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun sendResetEmail() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email required"
            etEmail.requestFocus()
            return
        }

        btnReset.isEnabled = false

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Password reset email sent. Check your inbox.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            .addOnFailureListener {
                btnReset.isEnabled = true
                Toast.makeText(
                    this,
                    it.message ?: "Failed to send reset email",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
