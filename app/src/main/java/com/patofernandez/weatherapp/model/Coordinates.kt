package com.patofernandez.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Coordinates (
    @SerializedName("lng")
    var longitude: Double = .0,
    @SerializedName("lat")
    var latitude: Double = .0
)