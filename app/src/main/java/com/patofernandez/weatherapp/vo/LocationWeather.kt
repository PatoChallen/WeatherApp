package com.patofernandez.weatherapp.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    indices = [
        Index("id")
    ]
)
data class LocationWeather (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @field:SerializedName("city")
    val city: String,
    @field:SerializedName("country")
    val country: String,
    @field:SerializedName("lat")
    val lat: Double,
    @field:SerializedName("lng")
    val lng: Double,
    @field:SerializedName("dt")
    val date: String,
    @field:SerializedName("temp")
    val temp: String,
    @field:SerializedName("tempMax")
    val tempMax: String,
    @field:SerializedName("tempMin")
    val tempMin: String,
    @field:SerializedName("feels_like")
    val feelsLike: String,
    @field:SerializedName("pressure")
    val pressure: String,
    @field:SerializedName("humidity")
    val humidity: String,
    @field:SerializedName("main")
    val weatherMain: String,
    @field:SerializedName("icon")
    val iconUrl: String,
    @field:SerializedName("clouds")
    val clouds: String,
    @field:SerializedName("windSpeed")
    val windSpeed: String
)
