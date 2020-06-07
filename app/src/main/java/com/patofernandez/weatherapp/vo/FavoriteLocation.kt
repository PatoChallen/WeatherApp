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
data class FavoriteLocation (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("country")
    val country: String?,
    @field:SerializedName("temp")
    val temp: String?,
    @field:SerializedName("tempMax")
    val tempMax: String?,
    @field:SerializedName("tempMin")
    val tempMin: String?,
    @field:SerializedName("iconUrl")
    val iconUrl: String?,
    @field:SerializedName("lat")
    val lat: Double = .0,
    @field:SerializedName("lng")
    val lng: Double = .0
){
    lateinit var coordinates: String
}