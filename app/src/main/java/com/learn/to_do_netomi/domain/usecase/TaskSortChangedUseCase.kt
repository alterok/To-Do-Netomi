package com.learn.to_do_netomi.domain.usecase

import com.learn.to_do_netomi.base.domain.BaseUseCase
import com.learn.to_do_netomi.domain.repository.TaskListPreferenceRepository
import com.learn.to_do_netomi.presentation.misc.TaskSort
import javax.inject.Inject

class TaskSortChangedUseCase @Inject constructor(private val taskListPreferenceRepository: TaskListPreferenceRepository) :
    BaseUseCase<TaskSort, Unit> {
    override fun invoke(data: TaskSort) {
        taskListPreferenceRepository.saveTaskListSorting(data)
    }
}