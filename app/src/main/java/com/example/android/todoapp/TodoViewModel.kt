package com.example.android.todoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.todoapp.database.Task
import com.example.android.todoapp.database.TaskDao
import com.example.android.todoapp.database.TaskDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    private var dataBaseInstance: TaskDatabase ?= null

    var tasksList = MutableLiveData<List<Task>>()

    fun setInstanceOfDb(dataBaseInstance: TaskDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun saveDataIntoDb(data: Task){
        dataBaseInstance?.taskDao()?.insertAll(data)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
            },{
            })?.let {
                compositeDisposable.add(it)
            }
    }

    fun getTaskData(){
        dataBaseInstance?.taskDao()?.getAllTasks()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                if(!it.isNullOrEmpty()){
                    tasksList.postValue(it)
                }else{
                    tasksList.postValue(listOf())
                }
                it?.forEach {
                    Log.v("TODOdo",it.title.toString())
                }
            },{
            })?.let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    fun deleteTask(task: Task) {
        dataBaseInstance?.taskDao()?.delete(task)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                getTaskData()
            },{
            })?.let {
                compositeDisposable.add(it)
            }
    }
}
