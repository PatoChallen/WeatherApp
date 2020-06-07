package com.patofernandez.weatherapp.vo

class CityDay {
    var date: String = ""
    var day: String = ""
    var sunrise: String = ""
    var sunset: String = ""
    var hours: ArrayList<DayHour> = ArrayList()

    fun getTempMax() =  hours.maxBy { it.tempMax }?.tempMax
    fun getTempMin() =  hours.minBy { it.tempMin }?.tempMin
    fun getIconUrl() =  hours.first().iconUrl
}