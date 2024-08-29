package com.learn.to_do_netomi.data.repository

import com.learn.to_do_netomi.data.local.ToDoTaskDatabase
import com.learn.to_do_netomi.data.local.mapper.toDomain
import com.learn.to_do_netomi.data.local.mapper.toEntity
import com.learn.to_do_netomi.domain.model.TaskDomainModel
import com.learn.to_do_netomi.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val TAG = "TaskRepositoryImpl"

class TaskRepositoryImpl @Inject constructor(private val database: ToDoTaskDatabase) :
    TaskRepository {

    override suspend fun addTask(task: TaskDomainModel) {
        database.getTaskDao().insertTask(task.toEntity())
    }

    override suspend fun removeTask(task: TaskDomainModel) {
        database.getTaskDao().removeTask(task.toEntity())
    }

    override suspend fun getAllTasks(): Flow<List<TaskDomainModel>> = flow {
        database.getTaskDao().getAllTasksFlow().collect { cachedTasks ->
            emit(cachedTasks.map { it.toDomain() })
        }
    }.flowOn(Dispatchers.IO)
}