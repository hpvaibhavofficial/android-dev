package com.example.geocoding

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class AttenTask: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atten_task)

        val editLatitude = findViewById<EditText>(R.id.editLatitude)
        val editLongitude = findViewById<EditText>(R.id.editLongitude)
        val btnShowAddress = findViewById<Button>(R.id.btnShowAddress)
        val tvAddress = findViewById<TextView>(R.id.tvAddress)

        btnShowAddress.setOnClickListener {
            val latText = editLatitude.text.toString()
            val lonText = editLongitude.text.toString()

            if (latText.isEmpty() || lonText.isEmpty()) {
                Toast.makeText(this, "Please enter both latitude and longitude", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val latitude = latText.toDouble()
            val longitude = lonText.toDouble()

            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    tvAddress.text = address
                } else {
                    tvAddress.text = "No address found for this location"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                tvAddress.text = "Error getting address: ${e.message}"
            }
        }
    }
}
