package com.learn.to_do_netomi.domain.repository

import android.util.Log
import com.learn.to_do_netomi.data.local.ToDoTaskDatabase
import com.learn.to_do_netomi.data.local.mapper.toDomain
import com.learn.to_do_netomi.data.local.mapper.toEntity
import com.learn.to_do_netomi.domain.model.TaskDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val TAG = "FakeTaskRepositoryImpl"

class FakeTaskRepositoryImpl @Inject constructor(private val database: ToDoTaskDatabase) :
    TaskRepository {

    override suspend fun addTask(task: TaskDomainModel) {
        database.getTaskDao().insertTask(task.toEntity())
    }

    override suspend fun removeTask(task: TaskDomainModel) {
        database.getTaskDao().removeTask(task.toEntity())
    }

    override suspend fun getAllTasks(): Flow<List<TaskDomainModel>> = channelFlow {
        database.getTaskDao().getAllTasksFlow().collectLatest { cachedTasks ->
            if (cachedTasks.isEmpty()) {
                //insert dummy data for testing
                val dummydata = TaskDomainModel.createDummyTasks(5).map { it.toEntity() }
                Log.i(TAG, "getAllTasks cached: ${coroutineContext}")
                database.getTaskDao().insertTasks(dummydata)
            } else {
                Log.i(TAG, "getAllTasks direct: ${coroutineContext.toString()}")
                send(cachedTasks.map { it.toDomain() })
            }
        }
    }.flowOn(Dispatchers.IO)

}