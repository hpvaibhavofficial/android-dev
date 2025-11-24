package com.example.weatherapi

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnFetch = findViewById<Button>(R.id.btnFetch)
        val tempText = findViewById<TextView>(R.id.tempText)

        // Observe data
        viewModel.temperature.observe(this, Observer { value ->
            tempText.text = value
        })

        // When user clicks button → call API
        btnFetch.setOnClickListener {
            viewModel.loadWeather("Delhi")
        }
    }
}