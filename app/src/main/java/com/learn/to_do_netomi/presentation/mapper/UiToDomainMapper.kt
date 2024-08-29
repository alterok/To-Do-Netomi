package com.learn.to_do_netomi.presentation.mapper

import com.learn.to_do_netomi.domain.model.TaskDomainModel
import com.learn.to_do_netomi.ui.model.Task

fun Task.toDomainModel() = TaskDomainModel(
    taskId,
    title,
    description,
    createdAt,
    deadlineTimestamp,
    taskStatus
)