package com.learn.to_do_netomi.ui.adapter

import com.learn.to_do_netomi.ui.model.Task

interface TaskItemClickListener {
    fun onTaskUpdated(oldTask: Task, newTask: Task)
    fun onRemoveClicked(task: Task)
    fun onTaskClicked(task: Task)
}
