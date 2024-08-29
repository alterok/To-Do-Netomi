package com.learn.to_do_netomi.ui.model

import com.learn.to_do_netomi.base.domain.TaskStatus

data class Task(
    val taskId: Long,
    val title: String,
    val description: String,
    val createdAt: Long,
    val deadlineTimestamp: Long,
    val taskStatus: TaskStatus,
)
