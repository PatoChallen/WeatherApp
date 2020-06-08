package com.patofernandez.weatherapp.vo

class CityDay {
    var date: String = ""
    var day: String = ""
    var hours: ArrayList<DayHour> = ArrayList()

    fun getTempMax() =  hours.maxBy { it.tempMax.removeSuffix("º").toInt() }?.tempMax
    fun getTempMin() =  hours.minBy { it.tempMin.removeSuffix("º").toInt() }?.tempMin
    fun getIconUrl() =  hours.first().iconUrl
}