package com.example.cse226

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        listView = findViewById(R.id.listView)
        val myItems = listOf(
            MyItem("Mobile App Development","This is an Android App", R.drawable.aa),
            MyItem("Android App Development","This is an Android App", R.drawable.aa),
            MyItem("IOS App Development","This is an Android App", R.drawable.aa),
            MyItem("Web App Development","This is an Android App", R.drawable.aa),
            MyItem(" App Development","This is an Android App", R.drawable.aa),
            MyItem("Mobile App Development","This is an Android App", R.drawable.aa)
        )

        val adapter = MyAdapter(this,myItems)
        listView.adapter =adapter

        listView.setOnItemClickListener{
                _,_,position,_ ->
            val item = myItems[position]
            Toast.makeText(this,"CLICKED : ${item.title}",Toast.LENGTH_LONG).show()
        }

//        listView.setOnItemClickListener(object :OnClickListener){
//            override fun onItemClick(
//                parent: AdapterView<*> ? , view: View?,
//                position : in
//            )
//        }
    }
}