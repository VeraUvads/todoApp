package com.example.android.todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,

    @ColumnInfo(name = "Title")
    val title: String?,

    @ColumnInfo(name = "Description")
    val description: String?
)