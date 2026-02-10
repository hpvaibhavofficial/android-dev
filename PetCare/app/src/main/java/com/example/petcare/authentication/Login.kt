package com.example.petcare.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.petcare.MainActivity
import com.example.petcare.R
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: AppCompatButton
    private lateinit var tvForgot: TextView
    private lateinit var tvSignup: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvForgot = findViewById(R.id.tvForgot)
        tvSignup = findViewById(R.id.tvSignup)

        btnLogin.setOnClickListener {
            loginUser()
        }

        tvForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        tvSignup.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email required"
            etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Password required"
            etPassword.requestFocus()
            return
        }

        tvForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        btnLogin.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                btnLogin.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message ?: "Login failed", Toast.LENGTH_LONG).show()
                }
            }
    }
}
