package com.patofernandez.weatherapp.model

data class WeatherSys (
    var type: Int = 0,
    var id: Int = 0,
    var country: String = "",
    var sunrise: Long = 0L,
    var sunset: Long = 0L
)