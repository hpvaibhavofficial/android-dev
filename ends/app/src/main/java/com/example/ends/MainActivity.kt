package com.example.ends

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val imgView = findViewById<ImageView>(R.id.imgView)
        val btnFetch = findViewById<Button>(R.id.btnFetch)

        btnFetch.setOnClickListener {
            fetchData(tvStatus, imgView)
        }
    }

    private fun fetchData(tv: TextView, img: ImageView) {

        lifecycleScope.launch {
            tv.text = "Fetching Data"
            img.setImageResource(R.drawable.ic_launcher_foreground)
            delay(1000)

            tv.text = "Loading Image 1..."
            img.setImageResource(R.drawable.ic_launcher_background)
            delay(1000)

            tv.text = "Loading Image 2..."
            img.setImageResource(R.drawable.ic_launcher_foreground)
            delay(2000)

            tv.text = "Loading Image 3..."
            img.setImageResource(R.drawable.ic_launcher_background)
            delay(4000)

            tv.text = "Done!"
            img.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }
}
