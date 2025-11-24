package com.example.mymusicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class ServiceActivity : Service() {
    private lateinit var player: MediaPlayer
    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.sample)
        player.isLooping = true
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> {
                if (!player.isPlaying) {
                    player.start()
                }
            }
            "PAUSE" -> {
                if (player.isPlaying) {
                    player.pause()
                }
            }
            "STOP" -> {
                if (player.isPlaying) {
                    player.stop()
                    player.prepare() // prepare again so it can play next time
                }
            }
            else -> {  // Default if no action given
                if (!player.isPlaying) {
                    player.start()
                }
            }
        }
        return START_NOT_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        if (player.isPlaying) {
            player.stop()
        }
                player.release()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}