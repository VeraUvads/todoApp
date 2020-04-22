package com.example.android.todoapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.todoapp.database.Task
import com.example.android.todoapp.database.TaskDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var viewModel: TodoViewModel? = null
    private var taskAdapter: TaskAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        var dataBaseInstance = TaskDatabase.getInstance(this)
        viewModel?.setInstanceOfDb(dataBaseInstance)

        initViews()
        setListeners()
        observerViewModel()
    }


    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter()
        recyclerView.adapter = taskAdapter
        onItemTouchHelper()
    }


    private fun setListeners() {
        buttonSave.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        var titleTask = editTextTitle.text.trim().toString()

        if (titleTask.isNullOrBlank()) {
            Toast.makeText(this, "Пожалуйста введите задачу", Toast.LENGTH_LONG).show()
        } else {
            var task = Task(title = titleTask)
            viewModel?.saveDataIntoDb(task)
            editTextTitle.text.clear()
            observerViewModel()
        }
    }

    private fun observerViewModel() {
        viewModel?.getTaskData()
        viewModel?.tasksList?.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                handleData(it)
            } else {
                handleZeroCase()
            }
        })
    }

    private fun handleData(data: List<Task>) {
        taskAdapter?.setData(data)
    }

    private fun handleZeroCase() {
        recyclerView.visibility = View.GONE
        Toast.makeText(this, "No Records Found", Toast.LENGTH_LONG).show()
    }

    private fun onItemTouchHelper() {

        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = viewModel?.tasksList?.value?.get(viewHolder.adapterPosition) as Task
                viewModel?.deleteTask(id)
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}
