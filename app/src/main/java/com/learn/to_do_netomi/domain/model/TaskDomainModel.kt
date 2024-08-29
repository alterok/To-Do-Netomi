package com.learn.to_do_netomi.domain.model

import com.learn.to_do_netomi.base.domain.TaskStatus
import kotlin.random.Random

data class TaskDomainModel(
    val taskId: Long = System.currentTimeMillis(),
    val title: String,
    val description: String,
    val createdAt: Long,
    val deadlineTimestamp: Long,
    val taskStatus: TaskStatus,
) {
    companion object {
        fun createDummyTasks(count: Int) = List(count) {
            TaskDomainModel(
                System.nanoTime(),
                "Dummy title ${Random.nextInt(1, 1000)}",
                "",
                createdAt = System.currentTimeMillis(),
                deadlineTimestamp = System.currentTimeMillis() + Random.nextInt(
                    -60000 * 25,
                    60000 * 25
                ),
                taskStatus = TaskStatus.RUNNING,
            )
        }
    }
}