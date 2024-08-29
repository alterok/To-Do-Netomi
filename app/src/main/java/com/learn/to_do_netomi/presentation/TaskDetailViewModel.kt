package com.learn.to_do_netomi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.to_do_netomi.domain.usecase.TaskListUseCases
import com.learn.to_do_netomi.presentation.mapper.toDomainModel
import com.learn.to_do_netomi.ui.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val taskListUseCases: TaskListUseCases) :
    ViewModel() {

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskListUseCases.addTaskUseCase(task.toDomainModel())
        }
    }

    fun removeTask(task: Task){
        viewModelScope.launch {
            taskListUseCases.removeTaskUseCase(task.toDomainModel())
        }
    }
}