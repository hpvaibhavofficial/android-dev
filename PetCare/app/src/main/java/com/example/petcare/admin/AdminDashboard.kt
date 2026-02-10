package com.example.petcare.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petcare.R
import androidx.appcompat.widget.AppCompatButton
import com.example.petcare.admin.pet.AddPet

class AdminDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        findViewById<AppCompatButton>(R.id.btnAddPet).setOnClickListener {
            startActivity(Intent(this, AddPet::class.java))
        }
    }
}
