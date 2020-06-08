package com.patofernandez.weatherapp.vo

import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ln

class City {
    var city: String = ""
    var country: String = ""
    var lat: Double = .0
    var lng: Double = .0
    val days: ArrayList<CityDay> = ArrayList()

    fun getLatLng() = LatLng(lat, lng)

}