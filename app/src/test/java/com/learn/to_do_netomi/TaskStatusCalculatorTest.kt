package com.learn.to_do_netomi

import com.learn.to_do_netomi.base.domain.TaskStatus
import com.learn.to_do_netomi.ui.util.TaskStatusCalculator
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TaskStatusCalculatorTest {
    private val calculator = TaskStatusCalculator()

    @Test
    fun testDeadlineTimestamp_forCompletedStatus() {
        val result = calculator.getTaskStatusFromDeadlineTimestamp(-1L, System.currentTimeMillis())
        assertEquals(TaskStatus.COMPLETED, result)
    }

    @Test
    fun testDeadlineTimestamp_forPendingStatus() {
        val currentTime = System.currentTimeMillis()
        val result = calculator.getTaskStatusFromDeadlineTimestamp(currentTime - 1000L, currentTime)
        assertEquals(TaskStatus.PENDING, result)
    }

    @Test
    fun testDeadlineTimestamp_forRunningStatus() {
        val currentTime = System.currentTimeMillis()
        val result = calculator.getTaskStatusFromDeadlineTimestamp(currentTime + 1000L, currentTime)
        assertEquals(TaskStatus.RUNNING, result)
    }
}