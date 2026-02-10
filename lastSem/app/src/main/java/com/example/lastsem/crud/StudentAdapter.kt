package com.example.lastsem.crud

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lastsem.R
import com.example.lastsem.models.Student
import com.google.firebase.database.FirebaseDatabase

class StudentAdapter(
    private val context: Context,
    private val list: ArrayList<Student>,
    private val onEditClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val tvSection: TextView = itemView.findViewById(R.id.tvSection)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)

        val btnEditIcon: ImageView = itemView.findViewById(R.id.btnEditIcon)
        val btnDeleteIcon: ImageView = itemView.findViewById(R.id.btnDeleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = list[position]

        holder.tvName.text = student.name ?: "No Name"
        holder.tvId.text = "ID: ${student.studentId ?: "-"}"
        holder.tvSection.text = "Section: ${student.section ?: "-"}"
        holder.tvPhone.text = "Phone: ${student.phone ?: "-"}"

        // ✅ PEN ICON (Edit)
        holder.btnEditIcon.setOnClickListener {
            onEditClick(student)
        }

        // ✅ BIN ICON (Delete)
        holder.btnDeleteIcon.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete ${student.name}?")
                .setPositiveButton("Yes") { _, _ ->
                    deleteStudent(student.studentId.toString())
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun deleteStudent(studentId: String) {
        if (studentId.isEmpty()) {
            Toast.makeText(context, "Student ID missing!", Toast.LENGTH_SHORT).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("Students")

        ref.child(studentId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted ✅", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Delete Failed ❌", Toast.LENGTH_SHORT).show()
            }
    }
}
