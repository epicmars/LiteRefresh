package com.androidpi.literefresh.sample.common.datetime

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    val ONE_SECONDS_MS = 1000
    val ONE_MINUTE_MS = ONE_SECONDS_MS * 60
    val ONE_HOUR_MS = ONE_MINUTE_MS * 60
    val ONE_DAY_MS = ONE_HOUR_MS * 24

    val FORMAT_STANDARD: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val FORMAT_DATE: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val FORMAT_TIME: DateFormat = SimpleDateFormat("HH:mm:ss")
    val FORMAT_TIME_HH_mm: DateFormat = SimpleDateFormat("HH:mm")

    fun formatDateTime(date: Date): String {
        return FORMAT_STANDARD.format(date)
    }

    fun formatDate(date: Date): String {
        return FORMAT_DATE.format(date)
    }

    fun formatTime(time: Date): String {
        return FORMAT_TIME.format(time)
    }

    fun formatTimeHHmm(time: Date): String {
        return FORMAT_TIME_HH_mm.format(time)
    }

    fun now(): Calendar {
        return Calendar.getInstance()
    }

    fun assembleDateTime(date: Date, time: Date): Date {
        val datetime = now()
        val timeCalendar = now()
        datetime.time = date
        timeCalendar.time = time
        datetime.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        datetime.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        datetime.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND))
        datetime.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND))
        return datetime.time
    }

    fun parseDateTime(string: String): Date {
        return FORMAT_STANDARD.parse(string)
    }

}
