package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
    private val favoriteLocations = weatherRepository.getFavoriteLocations()

    fun getWeatherForecast(): LiveData<WeatherForecastApiResponse> {
        return weatherForecastApiResponse
    }

    fun getWheaterHoursByDay(date: Long): List<CurrentWeatherApiResponse> {
        return weatherForecastApiResponse.value!!.list.filter { FormatUtils.formatedDay(it.date) == FormatUtils.formatedDay(date) }
    }

    fun getFavoriteLocations(): MutableLiveData<List<CurrentWeatherApiResponse>> {
        return favoriteLocations
    }

    fun addFavoriteLocation(latLng: LatLng) {
        weatherRepository.addFavoriteLocation(latLng)
    }

    fun getSelectedLocation(): MutableLiveData<CurrentWeatherApiResponse> {
        return weatherRepository.getSelectedLocation()
    }

    fun setCoordinates(lat: Double, lon: Double) {
        weatherRepository.setCoordinates(lat, lon)
    }

    fun setCurrentWeather(currentWeather: CurrentWeatherApiResponse) {
        currentWeatherApiResponseData.value = currentWeather
        weatherForecastApiResponse = weatherRepository.getWeatherForecastByCoords(currentWeather.coordinates!!.latitude, currentWeather.coordinates!!.longitude)
    }

}
