package com.patofernandez.weatherapp.vo

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
    indices = [
        Index("id")],
    primaryKeys = ["name"]
)
data class FavoriteLocation (
    val id: Int,
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
    val lat: String?,
    @field:SerializedName("lng")
    val lng: String?
){
    lateinit var coordinates: String
}