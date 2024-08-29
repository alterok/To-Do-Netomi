package com.learn.to_do_netomi.domain.usecase

import com.learn.to_do_netomi.base.domain.BaseUseCase
import com.learn.to_do_netomi.domain.repository.TaskListPreferenceRepository
import com.learn.to_do_netomi.presentation.misc.TaskFilter
import javax.inject.Inject

class TaskFilterChangedUseCase @Inject constructor(private val taskListPreferenceRepository: TaskListPreferenceRepository) :
    BaseUseCase<TaskFilter, Unit> {
    override fun invoke(data: TaskFilter) {
        taskListPreferenceRepository.saveTaskListFilter(data)
    }
}