package com.example.mapdual

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.ArrayAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var cityListView: ListView

    private val cities = mapOf(
        "New Delhi" to LatLng(28.6139, 77.2090),
        "Mumbai" to LatLng(19.0760, 72.8777),
        "Chennai" to LatLng(13.0827, 80.2707),
        "Kolkata" to LatLng(22.5726, 88.3639),
        "Bengaluru" to LatLng(12.9716, 77.5946)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityListView = findViewById(R.id.city_list)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cities.keys.toList())
        cityListView.adapter = adapter

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        cityListView.setOnItemClickListener { _, _, position, _ ->
            val cityName = adapter.getItem(position)
            val location = cities[cityName]
            if (location != null) {
                updateMapLocation(cityName!!, location)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val defaultCity = "New Delhi"
        val defaultLocation = cities[defaultCity]
        updateMapLocation(defaultCity, defaultLocation!!)
    }

    private fun updateMapLocation(cityName: String, location: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(location).title(cityName))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }
}
