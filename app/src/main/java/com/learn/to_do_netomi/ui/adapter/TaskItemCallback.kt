package com.learn.to_do_netomi.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.learn.to_do_netomi.ui.model.Task

object TaskItemCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId &&
                oldItem.taskStatus == newItem.taskStatus &&
                oldItem.title == newItem.title &&
                oldItem.description == newItem.description &&
                oldItem.deadlineTimestamp == newItem.deadlineTimestamp
    }
}