package com.example.roomdatabaseinandroid

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    @ColumnInfo(defaultValue = "0") val timestamp: Long = System.currentTimeMillis()
)
