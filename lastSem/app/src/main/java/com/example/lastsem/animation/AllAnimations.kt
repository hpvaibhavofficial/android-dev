package com.example.lastsem.animation

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.lastsem.R

class AllAnimations : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_animations)

        val star = findViewById<ImageView>(R.id.starImage)

        val animation = AnimationUtils.loadAnimation(
            this,
            R.anim.star_path
        )

        star.startAnimation(animation)

    }
}
