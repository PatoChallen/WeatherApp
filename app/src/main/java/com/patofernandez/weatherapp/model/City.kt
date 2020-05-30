package com.patofernandez.weatherapp.model

import com.google.gson.annotations.SerializedName

class City {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String = ""
    @SerializedName("coord")
    var coord: Coordinates? = null
    @SerializedName("country")
    var country: String = ""
    @SerializedName("timezone")
    var timezone: Long = 0L
    @SerializedName("sunrise")
    var sunrise: Long = 0L
    @SerializedName("sunset")
    var sunset: Long = 0L
}