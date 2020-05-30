package com.patofernandez.weatherapp.model

import com.google.gson.annotations.SerializedName

class WeatherForecastApiResponse {
    @SerializedName("cod")
    var cod: String = ""
    @SerializedName("message")
    var message: String = ""
    @SerializedName("cnt")
    var cnt: Int = 0
    @SerializedName("list")
    var list: List<CurrentWeatherApiResponse> = ArrayList()
    @SerializedName("city")
    var city: City? = null
}
