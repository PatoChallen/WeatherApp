package com.patofernandez.weatherapp.model

class OpenWeatherApiResponse {
    var coord: Coordinates? = null
    var weather: Weather? = null
    var base: String = ""
    var main: MainWeather? = null
    var visibility: Long = 0L
    var wind: Wind? = null
    var clouds: Clouds? = null
    var rain: Rain? = null
    var snow: Snow? = null
    var dt: Long = 0L
    var sys: WeatherSys? = null
    var timezone: Long = 0L
    var id: Long = 0L
    var name: String = ""
    var cod: Long = 0L
}