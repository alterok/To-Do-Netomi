package com.learn.to_do_netomi.data.local.mapper

import com.learn.to_do_netomi.data.local.model.TaskEntity
import com.learn.to_do_netomi.domain.model.TaskDomainModel

fun TaskEntity.toDomain() = TaskDomainModel(
    id,
    title,
    description,
    createdAt,
    deadlineTimestamp,
    taskStatus
)