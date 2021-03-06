package com.patofernandez.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Wind (
    @SerializedName("speed")
    var speed: Double = .0,
    @SerializedName("deg")
    var deg: Double = .0
)