package com.learn.to_do_netomi.domain.usecase

import com.learn.to_do_netomi.base.domain.SuspendableBaseUseCase
import com.learn.to_do_netomi.domain.model.TaskDomainModel
import com.learn.to_do_netomi.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) :
    SuspendableBaseUseCase<Unit, Flow<List<TaskDomainModel>>> {
    override suspend fun invoke(data: Unit): Flow<List<TaskDomainModel>> =
        withContext(Dispatchers.IO) {
            taskRepository.getAllTasks()
        }
}