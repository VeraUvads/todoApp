package com.example.android.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.todoapp.database.Task
import kotlinx.android.synthetic.main.task_item.view.*

class TaskAdapter (private val onDeleteClick: (Task) -> Unit) : RecyclerView.Adapter<TaskAdapter.dataViewHolder>() {
    private var taskList = mutableListOf<Task>()

    fun setData(list: List<Task>) {
        taskList.clear()
        taskList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dataViewHolder {
        return dataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
        ) {
            onDeleteClick.invoke(it)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: dataViewHolder, position: Int) {
        holder.itemView.title_item
        holder.setData(taskList[position])
    }

    inner class dataViewHolder(
        itemView: View, val onDeleteClick: (Task) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun setData(task: Task) {
            itemView.apply {
                title_item.text = task.title


            }

        }

    }
}