package com.patofernandez.weatherapp.model

class WeatherForecastApiResponse {
    var cod: String = ""
    var message: String = ""
    var cnt: Int = 0
    var list: List<CurrentWeatherApiResponse> = ArrayList()
    var city: City? = null
}
