package com.example.geocoding

import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addressInput = findViewById<EditText>(R.id.addressInput)
        val geoButton = findViewById<Button>(R.id.geoButton)
        val reverseGeoButton = findViewById<Button>(R.id.reverseGeoButton)
        val outputText = findViewById<TextView>(R.id.outputText)

        val geocoder = Geocoder(this, Locale.getDefault())

        geoButton.setOnClickListener {
            val address = addressInput.text.toString()
            if (address.isNotEmpty()) {
                try {
                    val locationList = geocoder.getFromLocationName(address, 1)
                    if (!locationList.isNullOrEmpty()) {
                        val location = locationList[0]
                        val lat = location.latitude
                        val lon = location.longitude
                        outputText.text = "Latitude: $lat\nLongitude: $lon"
                    } else {
                        outputText.text = "No location found."
                    }
                } catch (e: Exception) {
                    outputText.text = "Error: ${e.message}"
                }
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }

        reverseGeoButton.setOnClickListener {
            try {

                val lat = 28.6139
                val lon = 77.2090

                val addressList = geocoder.getFromLocation(lat, lon, 1)
                if (!addressList.isNullOrEmpty()) {
                    val address = addressList[0].getAddressLine(0)
                    outputText.text = "Address: $address"
                } else {
                    outputText.text = "No address found for these coordinates."
                }
            } catch (e: Exception) {
                outputText.text = "Error: ${e.message}"
            }
        }
    }
}
