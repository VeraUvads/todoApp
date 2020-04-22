package com.example.android.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( task: Task) : Completable

    @Delete
    fun delete(task: Task): Completable

    @Update
    fun update(task: Task) : Completable

    @Query("SELECT * FROM todo_task")
    fun getAllTasks(): Single<List<Task>>

//    @Query("SELECT * FROM todo_task WHERE Title LIKE :title LIMIT 1")
//    fun findByName(title: String, description: String): Task

}