package com.example.roomdatabaseinandroid

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabaseinandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels()
    private val adapter = NoteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe LiveData
        viewModel.allNotes.observe(this) { notes ->
            adapter.submitList(notes)
        }

        // Add note
        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()
            if (title.isNotEmpty() || content.isNotEmpty()) {
                val note = Note(title = title, content = content)
                viewModel.insert(note)
                binding.etTitle.text?.clear()
                binding.etContent.text?.clear()
            }
        }
    }
}
