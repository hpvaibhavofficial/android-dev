package com.example.kotlincoroutines

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val viewModel : MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val resultText = findViewById<TextView>(R.id.resultText)
        val btnFetch = findViewById<Button>(R.id.btnFetch)

        viewModel.result.observe(this, Observer{
            resultText.text = it
        })

        btnFetch.setOnClickListener {
            viewModel.startWork()
        }
    }
}