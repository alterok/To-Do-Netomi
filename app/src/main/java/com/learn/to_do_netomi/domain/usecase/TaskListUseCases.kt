package com.learn.to_do_netomi.domain.usecase

import javax.inject.Inject

data class TaskListUseCases @Inject constructor(
    val addTaskUseCase: AddTaskUseCase,
    val removeTaskUseCase: RemoveTaskUseCase,
    val getAllTasksUseCase: GetAllTasksUseCase,
    val getTaskListPrefsUseCase: GetTaskListPrefsUseCase,
    val taskFilterChangedUseCase: TaskFilterChangedUseCase,
    val taskSortChangedUseCase: TaskSortChangedUseCase
)