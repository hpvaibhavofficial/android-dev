package com.example.endterm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.bookRecycler)

        val bookList = arrayListOf(
            Book("Introduction ", "niki"),
            Book("Code", "kumar ")
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ItemAdapter(bookList) { book ->
            Toast.makeText(this, "Clicked: ${book.title}", Toast.LENGTH_SHORT).show()
        }
    }
}
