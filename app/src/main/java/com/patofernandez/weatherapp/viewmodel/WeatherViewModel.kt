package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.utils.FormatUtils

class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository.getInstance()
    private var currentWeatherApiResponseData = MutableLiveData<CurrentWeatherApiResponse>()
    private var weatherForecastApiResponse = MutableLiveData<WeatherForecastApiResponse>()

    fun getCurrentWeatherByCoords(lat: Double, lng: Double): LiveData<CurrentWeatherApiResponse> {
        currentWeatherApiResponseData = weatherRepository.getCurrentWeatherByCoords(lat, lng)
        return currentWeatherApiResponseData
    }

    fun getWeatherForecastByCoords(lat: Double, lng: Double): LiveData<WeatherForecastApiResponse> {
        weatherForecastApiResponse = weatherRepository.getWeatherForecastByCoords(lat, lng)
        return weatherForecastApiResponse
    }

    fun getWheaterHoursByDay(date: Long): List<CurrentWeatherApiResponse> {
        return weatherForecastApiResponse.value!!.list.filter { FormatUtils.formatedDay(it.date) == FormatUtils.formatedDay(date) }
    }

    fun getFavoriteLocations(): List<String> {
        return weatherRepository.getFavoriteLocations().map { Gson().toJson(it) }
    }

}
