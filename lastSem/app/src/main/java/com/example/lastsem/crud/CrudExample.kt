package com.example.lastsem.crud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lastsem.R
import com.example.lastsem.models.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CrudExample : AppCompatActivity() {

    private lateinit var etStudentId: EditText
    private lateinit var etStudentName: EditText
    private lateinit var etSection: EditText
    private lateinit var etPhone: EditText

    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnShow: Button

    private lateinit var rvStudents: RecyclerView
    private lateinit var studentList: ArrayList<Student>
    private lateinit var studentAdapter: StudentAdapter

    private lateinit var dbRef: DatabaseReference

    // ✅ Edit Mode Variable
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_example)

        // ✅ Link UI
        etStudentId = findViewById(R.id.etStudentId)
        etStudentName = findViewById(R.id.etStudentName)
        etSection = findViewById(R.id.etSection)
        etPhone = findViewById(R.id.etPhone)

        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnShow = findViewById(R.id.btnShow)

        rvStudents = findViewById(R.id.rvStudents)

        // ✅ Firebase Ref
        dbRef = FirebaseDatabase.getInstance().getReference("Students")

        // ✅ Recycler setup
        studentList = ArrayList()
        rvStudents.layoutManager = LinearLayoutManager(this)

        studentAdapter = StudentAdapter(
            context = this,
            list = studentList,
            onEditClick = { student ->
                enableEditMode(student)
            }
        )

        rvStudents.adapter = studentAdapter

        // ✅ Default mode
        setNormalMode()

        // ✅ Buttons
        btnAdd.setOnClickListener { addStudent() }
        btnUpdate.setOnClickListener { updateStudent() }
        btnDelete.setOnClickListener { deleteStudentByButton() }
        btnShow.setOnClickListener { fetchStudents() }

        // ✅ Auto Fetch
        fetchStudents()
    }

    // ✅ Enable Edit Mode when clicking Pen Icon
    private fun enableEditMode(student: Student) {
        isEditMode = true

        etStudentId.setText(student.studentId)
        etStudentName.setText(student.name)
        etSection.setText(student.section)
        etPhone.setText(student.phone)

        // ✅ Disable ID editing (so update does not create new record)
        etStudentId.isEnabled = false

        // ✅ Change mode buttons
        btnAdd.isEnabled = false
        btnUpdate.isEnabled = true
        btnDelete.isEnabled = true

        Toast.makeText(this, "Editing Student ID: ${student.studentId} ✏️", Toast.LENGTH_SHORT).show()

        // ✅ Scroll to top automatically (simple way)
        rvStudents.smoothScrollToPosition(0)
    }

    // ✅ Normal Mode
    private fun setNormalMode() {
        isEditMode = false

        etStudentId.isEnabled = true

        btnAdd.isEnabled = true
        btnUpdate.isEnabled = true
        btnDelete.isEnabled = true
    }

    // ✅ ADD
    private fun addStudent() {
        val id = etStudentId.text.toString().trim()
        val name = etStudentName.text.toString().trim()
        val section = etSection.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || section.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val student = Student(id, name, section, phone)

        dbRef.child(id).setValue(student)
            .addOnSuccessListener {
                Toast.makeText(this, "Student Added ✅", Toast.LENGTH_SHORT).show()
                clearFields()
                setNormalMode()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Add ❌", Toast.LENGTH_SHORT).show()
            }
    }

    // ✅ UPDATE
    private fun updateStudent() {
        val id = etStudentId.text.toString().trim()
        val name = etStudentName.text.toString().trim()
        val section = etSection.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        if (id.isEmpty()) {
            Toast.makeText(this, "Student ID missing!", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedStudent = Student(id, name, section, phone)

        dbRef.child(id).setValue(updatedStudent)
            .addOnSuccessListener {
                Toast.makeText(this, "Updated ✅", Toast.LENGTH_SHORT).show()
                clearFields()
                setNormalMode()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update Failed ❌", Toast.LENGTH_SHORT).show()
            }
    }

    // ✅ DELETE
    private fun deleteStudentByButton() {
        val id = etStudentId.text.toString().trim()

        if (id.isEmpty()) {
            Toast.makeText(this, "Enter Student ID to Delete!", Toast.LENGTH_SHORT).show()
            return
        }

        dbRef.child(id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Deleted ✅", Toast.LENGTH_SHORT).show()
                clearFields()
                setNormalMode()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Delete Failed ❌", Toast.LENGTH_SHORT).show()
            }
    }

    // ✅ FETCH STUDENTS
    private fun fetchStudents() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()

                for (studentSnap in snapshot.children) {
                    val student = studentSnap.getValue(Student::class.java)
                    if (student != null) {
                        studentList.add(student)
                    }
                }

                studentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CrudExample, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ✅ CLEAR INPUT
    private fun clearFields() {
        etStudentId.text.clear()
        etStudentName.text.clear()
        etSection.text.clear()
        etPhone.text.clear()
        etStudentId.isEnabled = true
    }
}
