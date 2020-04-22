package com.example.android.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.todoapp.database.Task
import kotlinx.android.synthetic.main.task_item.view.*

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.DataViewHolder>() {
    private var taskList = mutableListOf<Task>()

    fun setData(list: List<Task>) {
        taskList.clear()
        taskList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.setData(taskList[position])
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(task: Task) {
            itemView.apply {
                title_item.text = task.title
            }
        }
    }
}