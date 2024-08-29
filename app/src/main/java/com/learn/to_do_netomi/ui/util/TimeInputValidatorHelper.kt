package com.learn.to_do_netomi.ui.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val TAG = "TimeInputValidatorHelpe"
class TimeInputValidatorHelper {
    enum class ValidationResult(val msg: String) {
        VALID("Valid"),
        INVALID_12HR("Invalid Input For 12HR Format!"),
    }

    fun isValid12HrTimeInput(timeStr: String): ValidationResult {
        val values = timeStr.split(":")
        if (values.size != 2)
            return ValidationResult.INVALID_12HR
        val hr = values.first().toIntOrNull() ?: return ValidationResult.INVALID_12HR
        val min = values.last().toIntOrNull() ?: return ValidationResult.INVALID_12HR

        return if (hr <= 0 || hr > 12 || min >= 60 || min < 0) {
            ValidationResult.INVALID_12HR
        } else {
            ValidationResult.VALID
        }
    }

    fun convertTimeInputToMillis(timeStr: String, amPm: String): Long {
        if (isValid12HrTimeInput(timeStr) != ValidationResult.VALID)
            return -1L

        if (amPm.lowercase() != "am" && amPm.lowercase() != "pm")
            return -1L

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())

        val today = calendar.time

        val dateTimeStr = "${SimpleDateFormat("MM/dd/yyyy").format(today)} $timeStr $amPm"
        val dateTime = dateFormat.parse(dateTimeStr)

        return dateTime?.time ?: -1L
    }

    fun convertMillisToTodayTimeString(timeInMillis:Long) : String?{
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        if (timeInMillis in startOfDay..endOfDay) {
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return timeFormat.format(timeInMillis)
        }

        return null
    }
}