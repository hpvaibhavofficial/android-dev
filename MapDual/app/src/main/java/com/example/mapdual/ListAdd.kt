package com.example.mapdual

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ListAdd : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val locations = mutableListOf<String>()

    private val mapViewBundleKey = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_add)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize MapView and ListView
        mapView = findViewById(R.id.mapView)
        listView = findViewById(R.id.locationListView)

        // Setup adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locations)
        listView.adapter = adapter

        // MapView setup
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(mapViewBundleKey)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Add sample markers
        val cities = listOf(
            LatLng(28.6139, 77.2090) to "New Delhi",
            LatLng(19.0760, 72.8777) to "Mumbai",
            LatLng(13.0827, 80.2707) to "Chennai",
            LatLng(22.5726, 88.3639) to "Kolkata"
        )

        for ((coords, name) in cities) {
            googleMap.addMarker(MarkerOptions().position(coords).title(name))
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cities[0].first, 5f))

        // Handle marker click
        googleMap.setOnMarkerClickListener { marker ->
            val cityName = marker.title ?: "Unknown"
            if (!locations.contains(cityName)) {
                locations.add(cityName)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "$cityName added to list", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "$cityName is already in the list", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    // MapView lifecycle methods
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { mapView.onPause(); super.onPause() }
    override fun onDestroy() { mapView.onDestroy(); super.onDestroy() }
    override fun onLowMemory() { super.onLowMemory(); mapView.onLowMemory() }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(mapViewBundleKey)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(mapViewBundleKey, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }
}
