package com.patofernandez.weatherapp.model

import com.google.gson.annotations.SerializedName
import com.patofernandez.weatherapp.vo.LocationWeather

data class CurrentWeatherApiResponse (
    @SerializedName("coord")
    var coordinates: Coordinates? = null,
    @SerializedName("weather")
    var weather: List<Weather?> = ArrayList(),
    @SerializedName("base")
    var base: String = "",
    @SerializedName("main")
    var main: MainWeather? = null,
    @SerializedName("visibility")
    var visibility: Double = .0,
    @SerializedName("wind")
    var wind: Wind? = null,
    @SerializedName("clouds")
    var clouds: Clouds? = null,
    @SerializedName("rain")
    var rain: Rain? = null,
    @SerializedName("snow")
    var snow: Snow? = null,
    @SerializedName("dt")
    var date: Long = 0L,
    @SerializedName("sys")
    var sys: WeatherSys? = null,
    @SerializedName("timezone")
    var timezone: Long = 0L,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("cod")
    var cod: Long = 0L
)