package com.example.android.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao


    companion object {
        @Volatile
        private var databaseInstance: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            synchronized(this) {
                var instance = databaseInstance
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "todo_task"
                    ).fallbackToDestructiveMigration()
                        .build()
                    databaseInstance = instance
                }
                return instance
            }
        }
    }

}



