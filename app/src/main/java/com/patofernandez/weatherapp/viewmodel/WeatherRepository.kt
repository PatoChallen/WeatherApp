package com.patofernandez.weatherapp.viewmodel

import com.patofernandez.weatherapp.services.RetrofitService.createService
import com.patofernandez.weatherapp.services.OpenWeatherApi

class WeatherRepository {

    private val openWeatherApi: OpenWeatherApi = createService(
        OpenWeatherApi::class.java
    )

    companion object {
        private var weatherRepository: WeatherRepository? = null
        fun getInstance(): WeatherRepository {
            if (weatherRepository == null) {
                weatherRepository = WeatherRepository()
            }
            return weatherRepository!!
        }

        const val KEY = "4aacfcc02cb3fed3918e88987aaf6fc3"
    }

}