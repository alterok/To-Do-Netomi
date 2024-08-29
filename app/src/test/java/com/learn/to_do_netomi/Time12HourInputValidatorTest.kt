package com.learn.to_do_netomi

import com.learn.to_do_netomi.ui.util.TimeInputValidatorHelper.ValidationResult
import com.learn.to_do_netomi.ui.util.TimeInputValidatorHelper
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class Time12HourInputValidatorTest {
    private val timeValidator = TimeInputValidatorHelper()

    @Test
    fun testValid12HrTimeInput_ValidTime() {
        assertEquals(ValidationResult.VALID, timeValidator.isValid12HrTimeInput("02:30"))
        assertEquals(ValidationResult.VALID, timeValidator.isValid12HrTimeInput("12:32"))
    }

    @Test
    fun testInvalid12HrTimeInput_InvalidHours() {
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("00:30")) // Hours out of range
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("13:30")) // Hours out of range
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("25:30")) // Hours out of range
    }

    @Test
    fun testInvalid12HrTimeInput_InvalidMinutes() {
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("12:60")) // Minutes out of range
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("12:61")) // Minutes out of range
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("12:-1")) // Minutes negative
    }

    @Test
    fun testInvalid12HrTimeInput_InvalidFormat() {
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("02:30:45")) // Invalid format
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("0230")) // Invalid format
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("02")) // Invalid format
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput("02:")) // Missing minutes
        assertEquals(ValidationResult.INVALID_12HR, timeValidator.isValid12HrTimeInput(":30")) // Missing hours
    }
}