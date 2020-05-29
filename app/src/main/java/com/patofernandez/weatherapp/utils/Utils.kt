package com.patofernandez.weatherapp.utils

import android.text.format.DateFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class Utils {
    companion object {

        private const val KELVIN_CONSTANT = 273

        fun formatedKelvinToCelsius(temp: Double): String {
            return BigDecimal(temp - KELVIN_CONSTANT).setScale(1, RoundingMode.HALF_EVEN).toString().plus(" ºC")
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
                return@with DateFormat.format("dd/MM/yyyy hh:mm:ss", this.time).toString()
            }
        }

        fun formatedDay(time: Long): String {
            return with(Calendar.getInstance().apply {
                timeInMillis = time * 1000
            }) {
                return@with DateFormat.format("EEE", this.time).toString()
            }
        }

        fun formatedVisibility(visibility: Double): String {
            return BigDecimal(visibility / 1000).setScale(0, RoundingMode.HALF_EVEN).toString().plus(" Km")
        }

    }
}