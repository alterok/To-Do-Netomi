package com.learn.to_do_netomi.ui.util

import com.learn.to_do_netomi.base.domain.TaskStatus

private const val TAG = "TaskStatusCalculator"

class TaskStatusCalculator {
    fun getTaskStatusFromDeadlineTimestamp(
        deadlineTimestamp: Long,
        currentTimestamp: Long,
    ): TaskStatus {
        return if (deadlineTimestamp < 0) {
            TaskStatus.COMPLETED
        } else if (deadlineTimestamp < currentTimestamp) {
            TaskStatus.PENDING
        } else {
            TaskStatus.RUNNING
        }
    }
}