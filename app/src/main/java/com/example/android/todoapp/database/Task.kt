package com.example.android.todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo ( name  =  "id" )
    val uid: Int? = null,

    @ColumnInfo(name = "Title")
    val title: String?

)