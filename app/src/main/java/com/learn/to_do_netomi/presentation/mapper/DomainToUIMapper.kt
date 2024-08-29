package com.learn.to_do_netomi.presentation.mapper

import com.learn.to_do_netomi.domain.model.TaskDomainModel
import com.learn.to_do_netomi.ui.model.Task

fun TaskDomainModel.toUIModel() = Task(
    taskId,
    title,
    description,
    createdAt,
    deadlineTimestamp,
    taskStatus
)