package com.learn.to_do_netomi.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.learn.to_do_netomi.domain.model.TaskListPrefs
import com.learn.to_do_netomi.domain.repository.TaskListPreferenceRepository
import com.learn.to_do_netomi.presentation.misc.TaskFilter
import com.learn.to_do_netomi.presentation.misc.TaskSort
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

private const val TAG = "TaskListPreferenceRepos"

class TaskListPreferenceRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    TaskListPreferenceRepository {
    private val PREF_NAME_TASK_LIST = context.packageName + "_prefs_task_list"
    private val PREF_NAME_TASK_FILTER = context.packageName + "_pref_task_filter"
    private val PREF_NAME_TASK_SORT = context.packageName + "_pref_task_sort"

    override val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME_TASK_LIST, MODE_PRIVATE)

    private val _taskListPrefsFlow =
        MutableStateFlow(TaskListPrefs.DEFAULT)
    override val taskListPrefsFlow: Flow<TaskListPrefs>
        get() = _taskListPrefsFlow

    init {
        initPreferences()
    }

    override fun initPreferences() {
        val taskFilter = prefs.getInt(PREF_NAME_TASK_FILTER, 0).let {
            TaskFilter.fromId(it) ?: TaskFilter.ALL
        }

        val taskSort = prefs.getInt(PREF_NAME_TASK_SORT, 0).let {
            TaskSort.fromId(it) ?: TaskSort.DEFAULT
        }

        _taskListPrefsFlow.value = TaskListPrefs(taskFilter, taskSort)
    }

    override fun saveTaskListFilter(filter: TaskFilter) {
        prefs.edit().putInt(PREF_NAME_TASK_FILTER, filter.id).apply()
        if (filter.id != _taskListPrefsFlow.value.taskListFilter.id) {
            //filter changed;
            _taskListPrefsFlow.value =
                TaskListPrefs(filter, _taskListPrefsFlow.value.taskListSort)
        }
    }

    override fun saveTaskListSorting(sort: TaskSort) {
        prefs.edit().putInt(PREF_NAME_TASK_SORT, sort.id).apply()
        if (sort.id != _taskListPrefsFlow.value.taskListSort.id) {
            //sort changed;
            _taskListPrefsFlow.value =
                TaskListPrefs(_taskListPrefsFlow.value.taskListFilter, sort)
        }
    }
}