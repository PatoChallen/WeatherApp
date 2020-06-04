package com.patofernandez.weatherapp.vo

import com.google.gson.annotations.SerializedName

data class Coordinates (
    @SerializedName("lon")
    var longitude: Double = .0,
    @SerializedName("lat")
    var latitude: Double = .0
)