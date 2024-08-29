package com.learn.to_do_netomi.domain.usecase

import com.learn.to_do_netomi.base.domain.SuspendableBaseUseCase
import com.learn.to_do_netomi.domain.model.TaskDomainModel
import com.learn.to_do_netomi.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveTaskUseCase @Inject constructor(private val repo: TaskRepository) :
    SuspendableBaseUseCase<TaskDomainModel, Unit> {
    override suspend fun invoke(data: TaskDomainModel) {
        withContext(Dispatchers.IO) {
            repo.removeTask(data)
        }
    }
}
