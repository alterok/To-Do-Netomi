package com.learn.to_do_netomi.domain.repository

import android.content.SharedPreferences
import com.learn.to_do_netomi.domain.model.TaskListPrefs
import com.learn.to_do_netomi.presentation.misc.TaskFilter
import com.learn.to_do_netomi.presentation.misc.TaskSort
import kotlinx.coroutines.flow.Flow

interface TaskListPreferenceRepository {
    val prefs: SharedPreferences
    val taskListPrefsFlow : Flow<TaskListPrefs>

    fun initPreferences()
    fun saveTaskListFilter(filter: TaskFilter)
    fun saveTaskListSorting(sort: TaskSort)
}