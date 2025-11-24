package com.example.espressotestandroidproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameField = findViewById<EditText>(R.id.editTextName)
        val button = findViewById<Button>(R.id.btnShow)
        val result = findViewById<TextView>(R.id.textResult)

        button.setOnClickListener {
            val text = nameField.text.toString()
            result.text = "Hello, $text"
        }
    }
}