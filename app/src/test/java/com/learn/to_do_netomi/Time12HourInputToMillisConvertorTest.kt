package com.learn.to_do_netomi

import com.learn.to_do_netomi.ui.util.TaskStatusCalculator
import com.learn.to_do_netomi.ui.util.TimeInputValidatorHelper
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Time12HourInputToMillisConvertorTest {
    private val mTaskStatusCalculator = TimeInputValidatorHelper()

    @Test
    fun testConvertTimeInputToMillis_InvalidTime() {
        assertEquals(-1L, mTaskStatusCalculator.convertTimeInputToMillis("25:30", "PM"))
        assertEquals(-1L, mTaskStatusCalculator.convertTimeInputToMillis("02:60", "PM"))
        assertEquals(
            -1L,
            mTaskStatusCalculator.convertTimeInputToMillis("02:30", "AM/PM")
        )
    }

    @Test
    fun testConvertTimeInputToMillis_InvalidAMPM() {
        assertEquals(
            -1L,
            mTaskStatusCalculator.convertTimeInputToMillis("02:30", "MORNING")
        )

        assertEquals(-1L, mTaskStatusCalculator.convertTimeInputToMillis("02:30", ""))
        assertEquals(
            -1L,
            mTaskStatusCalculator.convertTimeInputToMillis("02:30", "PM PM")
        )
    }

    @Test
    fun testConvertTimeInputToMillis_InvalidTimeFormat() {
        assertEquals(-1L, mTaskStatusCalculator.convertTimeInputToMillis("02:30:45", "PM"))
        assertEquals(-1L, mTaskStatusCalculator.convertTimeInputToMillis("0230", "PM")) // Missing colon
        assertEquals(-1L, mTaskStatusCalculator.convertTimeInputToMillis("02:30", "")) // Missing AM/PM
    }

    @Test
    fun testConvertTimeInputToMillis_ValidInput() {
        val calendar = Calendar.getInstance()
        val today = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(calendar.time)

        val expectedMillis = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())
            .parse("$today 02:30 PM")?.time ?: -1L

        assertEquals(expectedMillis, mTaskStatusCalculator.convertTimeInputToMillis("02:30", "PM"))
    }
}