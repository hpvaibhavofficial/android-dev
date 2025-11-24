package com.example.servicesproject

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class MyStartedService: Service() {
    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this,"Started Service Created", Toast.LENGTH_LONG).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this,"Started Service Running", Toast.LENGTH_LONG).show()
        return START_STICKY;
    }

    override fun onDestroy() {
        Toast.makeText(this,"Started Service Destroyed", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}