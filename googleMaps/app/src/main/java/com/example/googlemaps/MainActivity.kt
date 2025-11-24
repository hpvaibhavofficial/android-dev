package com.example.googlemaps

import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize views
        searchEditText = findViewById(R.id.searchLocation)
        searchButton = findViewById(R.id.btnSearch)

        // Search button click
        searchButton.setOnClickListener {
            val locationName = searchEditText.text.toString().trim()
            if (locationName.isNotEmpty()) {
                searchLocation(locationName)
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Default location - Punjab
        val punjab = LatLng(31.1471, 75.3412)
        mMap.addMarker(MarkerOptions().position(punjab).title("Punjab"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punjab, 10f))
    }

    private fun searchLocation(locationName: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocationName(locationName, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val latLng = LatLng(address.latitude, address.longitude)

                mMap.addMarker(MarkerOptions().position(latLng).title(locationName))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

//package com.example.googlemaps
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.LatLngBounds
//import com.google.android.gms.maps.model.MarkerOptions
//
//class MainActivity : AppCompatActivity(), OnMapReadyCallback {
//
//    private lateinit var mMap: GoogleMap
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Define two locations
//        val punjab = LatLng(31.1471, 75.3412)
//        val sydney = LatLng(-34.0, 151.0)
//
//        // Add markers
//        mMap.addMarker(MarkerOptions().position(punjab).title("Marker in Punjab"))
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//
//        // Create bounds to include both locations
//        val bounds = LatLngBounds.Builder()
//            .include(punjab)
//            .include(sydney)
//            .build()
//
//        // Animate camera to show both locations within screen
//        val padding = 150 // offset from edges in pixels
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
//    }
//}
//
//
//
////package com.example.googlemaps
////import android.os.Bundle
////import androidx.appcompat.app.AppCompatActivity
////import com.google.android.gms.maps.CameraUpdateFactory
////import com.google.android.gms.maps.GoogleMap
////import com.google.android.gms.maps.OnMapReadyCallback
////import com.google.android.gms.maps.SupportMapFragment
////import com.google.android.gms.maps.model.LatLng
////import com.google.android.gms.maps.model.MarkerOptions
////
////
////
////
////class MainActivity : AppCompatActivity(), OnMapReadyCallback {
////
////    private lateinit var mMap: GoogleMap
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_main)
////        // Go to Google Cloud Console and set the project firstly.
////        val mapFragment = supportFragmentManager
////            .findFragmentById(R.id.map) as SupportMapFragment
////        mapFragment.getMapAsync(this)
////    }
////
//////    override fun onMapReady(googleMap: GoogleMap) {
//////        mMap = googleMap
//////
//////        // Example: Add a marker and move the camera
//////        val sydney = LatLng(-34.0, 151.0)
//////        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//////        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
//////    }
////
////    override fun onMapReady(googleMap: GoogleMap) {
////        mMap = googleMap
////
////        // Location for Punjab, India (approximate center)
////        val punjab = LatLng(31.1471, 75.3412)
////
////        // Add a marker at Punjab and move the camera
////        mMap.addMarker(MarkerOptions().position(punjab).title("Marker in Punjab"))
////        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punjab, 8f)) // 8f is a good state-level zoom
////                val sydney = LatLng(-34.0, 151.0)
////        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
////        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
////    }
////}