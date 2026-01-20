package com.example.lastsem.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.lastsem.Dashboard
import com.example.lastsem.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Dashboard::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed: Wrong credentials",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}
