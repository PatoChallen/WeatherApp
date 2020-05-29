package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.SystemUnits

class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository.getInstance()
    private var currentWeatherApiResponseData = MutableLiveData<CurrentWeatherApiResponse>()

    fun getWeatherByCoords(lat: Double, lon: Double): LiveData<CurrentWeatherApiResponse> {
        currentWeatherApiResponseData = weatherRepository.getWeatherByCoords(lat, lon)
        return currentWeatherApiResponseData
    }

}
