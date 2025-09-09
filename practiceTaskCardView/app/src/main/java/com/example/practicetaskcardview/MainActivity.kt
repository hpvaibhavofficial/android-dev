package com.example.practicetaskcardview

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        val items = listOf(
            CardItem(R.drawable.india, "Team India", "No. 1 Test Team", "Current World Test Champions"),
            CardItem(R.drawable.australia, "Australia", "No. 2 Test Team", "5-time World Cup winners"),
            CardItem(R.drawable.england, "England", "No. 3 Test Team", "Inventors of the game"),
            CardItem(R.drawable.nz, "New Zealand", "No. 4 Test Team", "2019 World Cup finalists"),
            CardItem(R.drawable.sa, "South Africa", "No. 5 Test Team", "Known as the Proteas"),
            CardItem(R.drawable.pak, "Pakistan", "No. 6 Test Team", "1992 World Cup champions")
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter  = CardAdapter(items){
                item -> Toast.makeText(this,"Clicked :  ${item.teamInfo}", Toast.LENGTH_LONG).show()
        }
        recyclerView.adapter = adapter


    }
}