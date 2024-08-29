package com.learn.to_do_netomi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.to_do_netomi.domain.model.TaskListPrefs
import com.learn.to_do_netomi.domain.usecase.TaskListUseCases
import com.learn.to_do_netomi.presentation.mapper.toUIModel
import com.learn.to_do_netomi.presentation.misc.TaskFilter
import com.learn.to_do_netomi.presentation.misc.TaskSort
import com.learn.to_do_netomi.ui.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TaskListViewModel"

@HiltViewModel
class TaskListViewModel @Inject constructor(private val taskListUseCases: TaskListUseCases) :
    ViewModel() {
    private val _allTasksFlow = MutableStateFlow<List<Task>>(emptyList())

    private val _taskListPrefsFlow = MutableStateFlow(TaskListPrefs.DEFAULT)
    val taskListPrefsFlow: Flow<TaskListPrefs> get() = _taskListPrefsFlow

    val sortedAllTasksFlow: Flow<List<Task>>

    init {
        viewModelScope.launch {
            taskListUseCases.getAllTasksUseCase(Unit).collect { taskDomainModelList ->
                _allTasksFlow.value = taskDomainModelList.map { it.toUIModel() }
            }
        }

        viewModelScope.launch {
            taskListUseCases.getTaskListPrefsUseCase(Unit).collect {
                _taskListPrefsFlow.value = it
            }
        }

        sortedAllTasksFlow = combine(_allTasksFlow, _taskListPrefsFlow) { tasks, taskListPrefs ->
            val sortedList = when (taskListPrefs.taskListSort) {
                TaskSort.DEFAULT -> tasks.sortedByDescending { it.createdAt }
                TaskSort.ASCENDING -> tasks.sortedBy { it.deadlineTimestamp }
                TaskSort.DESCENDING -> tasks.sortedByDescending { it.deadlineTimestamp }
            }

            sortedList
        }
    }

    fun onFilterChanged(newFilter: TaskFilter) =
        taskListUseCases.taskFilterChangedUseCase(newFilter)

    fun onSortChanged(newSort: TaskSort) = taskListUseCases.taskSortChangedUseCase(newSort)
}