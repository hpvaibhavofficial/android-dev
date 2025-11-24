package com.example.corotines

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var balance = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val num = findViewById<EditText>(R.id.edittext)

        button.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val amount = num.text.toString().toInt()

                    if (amount < balance) {
                        Toast.makeText(this@MainActivity, "Transferred successfully", Toast.LENGTH_SHORT).show()
                    } else {
                      throw Exception()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Transfer Failed: Invalid Input", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
