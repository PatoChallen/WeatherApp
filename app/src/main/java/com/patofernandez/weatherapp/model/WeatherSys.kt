package com.patofernandez.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherSys (
    @SerializedName("type")
    var type: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("country")
    var country: String = "",
    @SerializedName("sunrise")
    var sunrise: Long = 0L,
    @SerializedName("sunset")
    var sunset: Long = 0L
)