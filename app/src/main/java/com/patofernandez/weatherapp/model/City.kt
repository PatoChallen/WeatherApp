package com.patofernandez.weatherapp.model

class City {
    var id: Int = 0
    var name: String = ""
    var coord: Coordinates? = null
    var country: String = ""
    var timezone: Long = 0L
    var sunrise: Long = 0L
    var sunset: Long = 0L
}