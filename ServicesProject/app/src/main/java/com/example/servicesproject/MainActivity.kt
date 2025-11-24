package com.example.servicesproject

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var boundService: MyBoundService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyBoundService.LocalBinder
            boundService = binder.getService()
            isBound = true
            Toast.makeText(this@MainActivity, boundService?.getWelcomeMessage(), Toast.LENGTH_LONG).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 🔥 ADD THIS (Required for Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        val btnStartService = findViewById<Button>(R.id.btnStartService)
        val btnStopService = findViewById<Button>(R.id.btnStopService)
        val btnBindService = findViewById<Button>(R.id.btnBindService)
        val btnUnbindService = findViewById<Button>(R.id.btnUnbindService)
        val btnStartForeground = findViewById<Button>(R.id.btnStartForeground)
        val btnStopForeground = findViewById<Button>(R.id.btnStopForeground)

        // Start Started Service
        btnStartService.setOnClickListener {
            val intent = Intent(this, MyStartedService::class.java)
            startService(intent)
            Toast.makeText(this, "Started Service Started", Toast.LENGTH_SHORT).show()
        }

        // Stop Started Service
        btnStopService.setOnClickListener {
            val intent = Intent(this, MyStartedService::class.java)
            stopService(intent)
            Toast.makeText(this, "Started Service Stopped", Toast.LENGTH_SHORT).show()
        }

        // Bind Bound Service
        btnBindService.setOnClickListener {
            val intent = Intent(this, MyBoundService::class.java)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
            Toast.makeText(this, "Binding to Bound Service", Toast.LENGTH_SHORT).show()
        }

        // Unbind Bound Service
        btnUnbindService.setOnClickListener {
            if (isBound) {
                unbindService(connection)
                isBound = false
                Toast.makeText(this, "Bound Service Unbound", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Service is not bound", Toast.LENGTH_SHORT).show()
            }
        }

        // Start Foreground Service
        btnStartForeground.setOnClickListener {
            val intent = Intent(this, MyForegroundService::class.java)
            startForegroundService(intent)
            Toast.makeText(this, "Foreground Service Started", Toast.LENGTH_SHORT).show()
        }

        // Stop Foreground Service
        btnStopForeground.setOnClickListener {
            val intent = Intent(this, MyForegroundService::class.java)
            stopService(intent)
            Toast.makeText(this, "Foreground Service Stopped", Toast.LENGTH_SHORT).show()
        }
    }
}
