package com.learn.to_do_netomi.domain.model

import com.learn.to_do_netomi.presentation.misc.TaskFilter
import com.learn.to_do_netomi.presentation.misc.TaskSort

data class TaskListPrefs(
    val taskListFilter: TaskFilter,
    val taskListSort: TaskSort,
){
    companion object{
        val DEFAULT = TaskListPrefs(TaskFilter.ALL, TaskSort.DEFAULT)
    }
}