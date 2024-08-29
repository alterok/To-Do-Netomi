package com.learn.to_do_netomi.domain.usecase

import com.learn.to_do_netomi.base.domain.BaseUseCase
import com.learn.to_do_netomi.domain.model.TaskListPrefs
import com.learn.to_do_netomi.domain.repository.TaskListPreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskListPrefsUseCase @Inject constructor(private val taskListPreferenceRepository: TaskListPreferenceRepository) :
    BaseUseCase<Unit, Flow<TaskListPrefs>> {
    override fun invoke(data: Unit): Flow<TaskListPrefs> = taskListPreferenceRepository.taskListPrefsFlow
}