package com.example.lastsem.crud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lastsem.R
import com.google.firebase.database.*

class CrudExample : AppCompatActivity() {

    private lateinit var etUid: EditText
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText

    private lateinit var btnCreate: Button
    private lateinit var btnRead: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_example)

        etUid = findViewById(R.id.etUid)
        etName = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)

        btnCreate = findViewById(R.id.btnCreate)
        btnRead = findViewById(R.id.btnRead)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        database = FirebaseDatabase.getInstance().getReference("users")
        setupListeners()
    }

    private fun setupListeners() {

        btnCreate.setOnClickListener {
            val uidText = etUid.text.toString().trim()
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (uidText.isEmpty() || name.isEmpty() || email.isEmpty()) {
                toast("All fields required")
                return@setOnClickListener
            }

            val uid = uidText.toInt()
            val user = User(uid, name, email)

            database.child(uid.toString()).setValue(user).addOnSuccessListener {
                    toast("User created")
                    clearFields()
                }.addOnFailureListener {
                    toast("Create failed")
                }
        }

        btnRead.setOnClickListener {
            val uidText = etUid.text.toString().trim()
            if (uidText.isEmpty()) {
                toast("UID required")
                return@setOnClickListener
            }

            val uid = uidText.toInt()

            database.child(uid.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val name = snapshot.child("name").getValue(String::class.java)
                            val email = snapshot.child("email").getValue(String::class.java)
                            etName.setText(name ?: "")
                            etEmail.setText(email ?: "")
                            toast("User loaded")
                        } else {
                            toast("User not found")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        toast(error.message)
                    }
                })
        }

        btnUpdate.setOnClickListener {
            val uidText = etUid.text.toString().trim()
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (uidText.isEmpty()) {
                toast("UID required")
                return@setOnClickListener
            }

            val uid = uidText.toInt()

            val updates = mapOf(
                "name" to name,
                "email" to email
            )

            database.child(uid.toString()).updateChildren(updates)
                .addOnSuccessListener {
                    toast("User updated")
                }
                .addOnFailureListener {
                    toast("Update failed")
                }
        }

        btnDelete.setOnClickListener {
            val uidText = etUid.text.toString().trim()
            if (uidText.isEmpty()) {
                toast("UID required")
                return@setOnClickListener
            }

            val uid = uidText.toInt()

            database.child(uid.toString()).removeValue().addOnSuccessListener {
                    toast("User deleted")
                    clearFields()
                }.addOnFailureListener {
                    toast("Delete failed")
                }
        }
    }

    private fun clearFields() {
        etUid.text.clear()
        etName.text.clear()
        etEmail.text.clear()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
