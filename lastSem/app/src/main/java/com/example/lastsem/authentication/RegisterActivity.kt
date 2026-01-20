package com.example.lastsem.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.lastsem.MainActivity
import com.example.lastsem.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        btnRegister.setOnClickListener {

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

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this, task.exception?.message ?: "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
}
