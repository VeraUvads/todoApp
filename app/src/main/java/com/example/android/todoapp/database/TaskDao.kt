package com.example.android.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert
    fun insertAll(vararg tasks: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM todo_task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM todo_task ORDER BY uid DESC")
    fun getAllNights(): LiveData<List<Task>>

    @Query("SELECT * FROM todo_task WHERE uid IN (:uid)")
    fun loadAllByIds(uid: IntArray): List<Task>

    @Query("SELECT * FROM todo_task WHERE Title LIKE :title AND Description LIKE :description LIMIT 1")
    fun findByName(title: String, description: String): Task

}