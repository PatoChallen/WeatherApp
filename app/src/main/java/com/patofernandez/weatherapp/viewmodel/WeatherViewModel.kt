package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.api.OpenWeatherService
import com.patofernandez.weatherapp.vo.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.vo.WeatherForecastApiResponse
import com.patofernandez.weatherapp.utils.FormatUtils
import retrofit2.Call

class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository(AppExecutors(), object : OpenWeatherService{
        override fun getWeather(appid: String): Call<CurrentWeatherApiResponse> {
            TODO("Not yet implemented")
        }

        override fun getCurrentWeatherByCoords(
            lat: Double,
            lon: Double,
            lang: String,
            appid: String
        ): Call<CurrentWeatherApiResponse> {
            TODO("Not yet implemented")
        }

        override fun getWeatherForecastByCoords(
            lat: Double,
            lon: Double,
            lang: String,
            appid: String
        ): Call<WeatherForecastApiResponse> {
            TODO("Not yet implemented")
        }

    })
    private var currentWeatherApiResponseData = MutableLiveData<CurrentWeatherApiResponse>()
    private var weatherForecastApiResponse = MutableLiveData<WeatherForecastApiResponse>()

    fun getWeatherForecast(): LiveData<WeatherForecastApiResponse> {
        return weatherForecastApiResponse
    }

    fun getWheaterHoursByDay(date: Long): List<CurrentWeatherApiResponse> {
        return weatherForecastApiResponse.value!!.list.filter { FormatUtils.formatedDay(it.date) == FormatUtils.formatedDay(date) }
    }

    fun getFavoriteLocations(): MutableLiveData<List<CurrentWeatherApiResponse>> {
        return weatherRepository.getFavoriteLocations()
    }

    fun addSelectedLocationToFavorites() {
        weatherRepository.addSelectedLocationToFavorites()
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

    fun removeFavoriteLocation(favoriteLocation: CurrentWeatherApiResponse) {
        weatherRepository.removeFavoriteLocation(favoriteLocation)
    }

}
