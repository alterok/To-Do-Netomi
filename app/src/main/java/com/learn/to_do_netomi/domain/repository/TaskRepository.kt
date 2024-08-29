package com.learn.to_do_netomi.domain.repository

import com.learn.to_do_netomi.domain.model.TaskDomainModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(task:TaskDomainModel)
    suspend fun removeTask(task: TaskDomainModel)
    suspend fun getAllTasks() : Flow<List<TaskDomainModel>>
}