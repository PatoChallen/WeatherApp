package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.utils.Utils

class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository.getInstance()
    private var currentWeatherApiResponseData = MutableLiveData<CurrentWeatherApiResponse>()
    private var weatherForecastApiResponse = MutableLiveData<WeatherForecastApiResponse>()

    fun getCurrentWeatherByCoords(lat: Double, lon: Double): LiveData<CurrentWeatherApiResponse> {
        currentWeatherApiResponseData = weatherRepository.getCurrentWeatherByCoords(lat, lon)
        return currentWeatherApiResponseData
    }

    fun getWeatherForecastByCoords(lat: Double, lon: Double): LiveData<WeatherForecastApiResponse> {
        weatherForecastApiResponse = weatherRepository.getWeatherForecastByCoords(lat, lon)
        return weatherForecastApiResponse
    }

    fun getWheaterHoursByDay(date: Long): List<CurrentWeatherApiResponse> {
        return weatherForecastApiResponse.value!!.list.filter { Utils.formatedDay(it.date) == Utils.formatedDay(date) }
    }

}
