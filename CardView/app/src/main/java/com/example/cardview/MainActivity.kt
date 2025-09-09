package com.example.cardview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        val items = listOf(
            CardItem("Apple", "This is an apple", R.drawable.apple),
            CardItem("Banana", "A sweet yellow fruit", R.drawable.banana),
            CardItem("Orange", "Citrus fruit rich in Vitamin C", R.drawable.orange),
            CardItem("Grapes", "Small juicy fruits in clusters", R.drawable.grapes),
            CardItem("Mango", "Tropical fruit, very sweet", R.drawable.mango),
            CardItem("Pineapple", "Spiky fruit with juicy yellow flesh", R.drawable.pineapple)
        )

       recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter  = CardAdapter(items){
            item -> Toast.makeText(this,"Clicked :  ${item.title}",Toast.LENGTH_LONG).show()
        }
    recyclerView.adapter = adapter
    }

}
