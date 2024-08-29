package com.learn.to_do_netomi.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.learn.to_do_netomi.base.domain.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("createdAt")
    val createdAt: Long,
    @ColumnInfo("deadlineTimestamp")
    val deadlineTimestamp: Long,
    @ColumnInfo("taskStatus")
    val taskStatus: TaskStatus
)