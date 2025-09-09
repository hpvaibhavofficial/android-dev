package com.example.customgridview

import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Grid
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.gridView)
        val items = listOf(
            GridItem("Apple",R.drawable.apple,"tHIS IS APPLE"),
            GridItem("Mango",R.drawable.mango,"This is mango"),
            GridItem("Banana",R.drawable.banana,"This is Banana"),
            GridItem("Guava",R.drawable.guava,"This is Guava"),
            GridItem("Grapes",R.drawable.grapes,"This is Grapes"),
            GridItem("Orange",R.drawable.orange,"This is Orange"),
            GridItem("Pineapple",R.drawable.pineapple,"This is Pineapple"),
            GridItem("Coconut",R.drawable.coconut,"This is Coconut")
        )

        val adapter = GridAdapter(this,items)
        gridView.adapter = adapter

        gridView.setOnItemClickListener(){
                _,_,position,_ -> val item = items[position]
            Toast.makeText(this,"Clicked :  ${item.name}",Toast.LENGTH_LONG).show()
        }
    }
}