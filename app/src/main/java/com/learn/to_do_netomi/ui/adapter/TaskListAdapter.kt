package com.learn.to_do_netomi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.learn.to_do_netomi.databinding.ItemviewTaskListBinding
import com.learn.to_do_netomi.ui.adapter.viewholder.TaskListViewHolder
import com.learn.to_do_netomi.ui.model.Task

class TaskListAdapter(private val taskItemClickListener: TaskItemClickListener) :
    ListAdapter<Task, TaskListViewHolder>(TaskItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        return TaskListViewHolder(
            ItemviewTaskListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            taskItemClickListener
        )
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}

