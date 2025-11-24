package com.example.mymusicplayer

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playBtn = findViewById<Button>(R.id.playBtn)
        val pauseBtn = findViewById<Button>(R.id.pauseBtn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)

        playBtn.setOnClickListener {
            val intent = Intent(this, ServiceActivity::class.java)
            intent.action = "PLAY"
            startService(intent)
        }

        pauseBtn.setOnClickListener {
            val intent = Intent(this, ServiceActivity::class.java)
            intent.action = "PAUSE"
            startService(intent)
        }

        stopBtn.setOnClickListener {
            val intent = Intent(this, ServiceActivity::class.java)
            intent.action = "STOP"
            startService(intent)
        }
    }
}
