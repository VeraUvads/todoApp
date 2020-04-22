package com.example.android.todoapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.todoapp.database.Task
import com.example.android.todoapp.database.TaskDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_popup_item.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: TodoViewModel
    lateinit var taskAdapter: TaskAdapter
    private lateinit var popUp: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        val dataBaseInstance = TaskDatabase.getInstance(this)
        viewModel.setInstanceOfDb(dataBaseInstance)

        initViews()
        setListeners()
        observerViewModel()
    }


    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter()
        recyclerView.adapter = taskAdapter
    }


    private fun setListeners() {
        buttonSave.setOnClickListener {
            saveData()
        }
        onItemTouchHelper()
        taskAdapter.setOnItemClickListener(object : TaskAdapter.OnItemClickListener {
            override fun onLongClick(position: Int) {
                val updatingTask = viewModel.tasksList.value?.get(position) as Task
                iniPop(updatingTask)
            }
        })
    }

    private fun saveData() {
        val titleTask = editTextTitle.text.trim().toString()

        if (titleTask.isBlank()) {
            Toast.makeText(this, "Пожалуйста введите задачу", Toast.LENGTH_LONG).show()
        } else {
            val task = Task(title = titleTask)
            viewModel.saveDataIntoDb(task)
            editTextTitle.text.clear()
            observerViewModel()
        }
    }

    private fun observerViewModel() {
        viewModel.getTaskData()
        viewModel.tasksList.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                handleData(it)
            } else {
                handleZeroCase()
            }
        })
    }

    private fun handleData(data: List<Task>) {
        taskAdapter.setData(data)
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
                val id = viewModel.tasksList.value?.get(viewHolder.adapterPosition) as Task
                viewModel.deleteTask(id)
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun iniPop(task: Task) {
        popUp = Dialog(this)
        popUp.setContentView(R.layout.update_popup_item)
        popUp.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popUp.window
            ?.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT)
        popUp.window?.attributes?.gravity = Gravity.TOP
        popUp.show()

        popUp.updateTitle.setText(task.title.toString())

        popUp.buttonUpdate.setOnClickListener {
            val updatingText = popUp.updateTitle.text.toString()

            if (updatingText.isNotBlank()) {
                val newTask = Task(uid = task.uid, title = popUp.updateTitle.text.toString())
                viewModel.updateTask(newTask)
                popUp.dismiss()
            } else {
                Toast.makeText(this, "Поле не должно быть пустым", Toast.LENGTH_LONG).show()
            }
        }
    }

}
