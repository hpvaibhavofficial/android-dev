package com.example.roomdatabaseinandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabaseinandroid.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteVH>() {
    private var notes: List<Note> = emptyList()

    fun submitList(list: List<Note>) {
        notes = list
        notifyDataSetChanged()
    }

    inner class NoteVH(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tvTitle.text = note.title
            binding.tvContent.text = note.content
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            binding.tvTimestamp.text = sdf.format(Date(note.timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteVH(binding)
    }

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size
}
