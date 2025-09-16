package com.example.android_ca

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sampleItems = listOf(
            CardItem("Task - 1", "15-09-2025", "Mark as Done"),
            CardItem("Task - 2", "17-09-2025", "Mark as Done"),
            CardItem("Task - 3", "19-09-2025", "Mark as Done"),
            CardItem("Task - 4", "21-09-2025", "Mark as Done"),
            CardItem("Task - 5", "23-09-2025", "Mark as Done")
        )


        val adapter = CardAdapter(sampleItems) { cardItem ->
            Toast.makeText(this, "Clicked item is ${cardItem.title}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
    }
}
