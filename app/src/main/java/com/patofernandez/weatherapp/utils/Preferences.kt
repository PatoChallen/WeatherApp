package com.patofernandez.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.patofernandez.weatherapp.application.WeatherApplication

enum class Preferences {
    favoriteLocations;

    val string: String
        get() = preferences().getString(name, "").toString()

    fun removeFromFavorites(latLng: LatLng) {
        val locations = string.removeSuffix("|").split("|").filterNot { it.contains(Gson().toJson(latLng)) }
        var updateData = ""
        locations.forEach { updateData += it.plus("|") }
        preferences().edit().putString(name, updateData).apply()
    }

    fun addToFavorite(latLng: LatLng) {
        preferences().edit().putString(name, string.plus(Gson().toJson(latLng)).plus("|")).apply()
    }

    fun getFavoriteLocations(): List<LatLng> {
        return string.split("|").mapNotNull { Gson().fromJson(it, LatLng::class.java) }
    }

    fun clear() {
        preferences().edit().putString(name, "").apply()
    }

    companion object {
        private var preferences: SharedPreferences? = null
        private fun preferences(): SharedPreferences {
            if (preferences == null) {
                preferences = WeatherApplication.getInstance().getSharedPreferences("WeatherPreferences", Context.MODE_PRIVATE)
            }
            return preferences!!
        }

    }
}