package com.patofernandez.weatherapp.vo

import com.google.gson.annotations.SerializedName

data class Rain (
    @SerializedName("1h")
    var oneHour: String = "",
    @SerializedName("3h")
    var threeHours: String = ""
)