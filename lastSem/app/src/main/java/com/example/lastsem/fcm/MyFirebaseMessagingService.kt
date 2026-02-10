package com.example.lastsem.fcm


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Runs when message comes (mostly for data messages)
        Log.d("FCM_MSG", "From: ${message.from}")
        Log.d("FCM_MSG", "Data: ${message.data}")
    }
}
