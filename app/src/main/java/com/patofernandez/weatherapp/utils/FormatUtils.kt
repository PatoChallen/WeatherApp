package com.patofernandez.weatherapp.utils

import android.text.format.DateFormat
import java.util.*

class FormatUtils {
    companion object {

        private const val KELVIN_CONSTANT = 273
        private const val REFRESH_TIME_IN_HOURS = 3

        fun formatedKelvinToCelsius(temp: Double?): String? {
            temp?.let {
                return (temp.toInt() - KELVIN_CONSTANT).toString().plus("º")
            }
            return null
        }

        fun formatedTime(time: Long): String {
            return with(Calendar.getInstance().apply {
                timeInMillis = time * 1000
            }) {
                return@with "${this[Calendar.HOUR_OF_DAY]}:${this[Calendar.MINUTE]}"
            }
        }

        fun formatedDate(time: Long): String {
            return with(Calendar.getInstance().apply {
                timeInMillis = time * 1000
            }) {
                return@with DateFormat.format("EEE dd 'de' MMMM", this.time).toString().capitalize()
            }
        }

        fun formatedDay(time: Long): String {
            return with(Calendar.getInstance().apply {
                timeInMillis = time * 1000
            }) {
                return@with DateFormat.format("EEE", this.time).toString()
            }
        }

        fun formatedHour(time: Long): String {
            return with(Calendar.getInstance().apply {
                timeInMillis = time * 1000
            }) {
                val initHour: String = DateFormat.format("HH:mm", this.time).toString()
                this.add(Calendar.HOUR, REFRESH_TIME_IN_HOURS)
                val endHour: String = DateFormat.format("HH:mm", this.time).toString()
                return@with "$initHour - $endHour"
            }
        }

        fun millisToCalendar(millis: Long): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = millis
            }
        }

    }
}