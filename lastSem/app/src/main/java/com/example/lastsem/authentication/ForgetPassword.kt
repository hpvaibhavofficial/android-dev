package com.example.lastsem.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.lastsem.R
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var btnSendLink: Button
    private lateinit var tvBackToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_password)
        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etResetEmail)
        btnSendLink = findViewById(R.id.btnSendLink)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)

        // Back to Login
        tvBackToLogin.setOnClickListener {
            finish()
        }

        btnSendLink.setOnClickListener {

            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter a valid email"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Password reset link sent to your email",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Error: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}
