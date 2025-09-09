package com.example.activity_chapter_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var heading: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // References
        recyclerView = findViewById(R.id.recyclerView)
        heading = findViewById(R.id.heading)

        // Heading text
        heading.text = "Explore Categories"

        // Categories
        val categories = listOf(
            Category("Android", "Companies related to Android ecosystem", R.drawable.android),
            Category("iOS", "Companies in iOS ecosystem", R.drawable.ios),
            Category("Electrical", "Top Electrical companies", R.drawable.electrical),
            Category("React", "Companies using React technology", R.drawable.react)
        )

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CategoryAdapter(categories) { category ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("CATEGORY_NAME", category.name)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}
