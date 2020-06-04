package com.patofernandez.weatherapp.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.patofernandez.weatherapp.repository.WeatherRepository
import com.patofernandez.weatherapp.vo.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.vo.WeatherForecastApiResponse
import com.patofernandez.weatherapp.utils.FormatUtils
import com.patofernandez.weatherapp.vo.Resource
import javax.inject.Inject

class WeatherViewModel  @Inject constructor(weatherRepository: WeatherRepository) : ViewModel() {

    val results: LiveData<Resource<List<CurrentWeatherApiResponse>>> = weatherRepository.getFavoriteLocations()


//    fun getWeatherForecast(): LiveData<WeatherForecastApiResponse> {
//        return weatherForecastApiResponse
//    }
//
//    fun getWheaterHoursByDay(date: Long): List<CurrentWeatherApiResponse> {
//        return weatherForecastApiResponse.value!!.list.filter { FormatUtils.formatedDay(it.date) == FormatUtils.formatedDay(date) }
//    }
//
//    fun getFavoriteLocations(): MutableLiveData<List<CurrentWeatherApiResponse>> {
//        return weatherRepository.getFavoriteLocations()
//    }
//
//    fun addSelectedLocationToFavorites() {
//        weatherRepository.addSelectedLocationToFavorites()
//    }
//
//    fun getSelectedLocation(): MutableLiveData<CurrentWeatherApiResponse> {
//        return weatherRepository.getSelectedLocation()
//    }
//
//    fun setCoordinates(lat: Double, lon: Double) {
//        weatherRepository.setCoordinates(lat, lon)
//    }
//
//    fun setCurrentWeather(currentWeather: CurrentWeatherApiResponse) {
//        currentWeatherApiResponseData.value = currentWeather
//        weatherForecastApiResponse = weatherRepository.getWeatherForecastByCoords(currentWeather.coordinates!!.latitude, currentWeather.coordinates!!.longitude)
//    }
//
//    fun removeFavoriteLocation(favoriteLocation: CurrentWeatherApiResponse) {
//        weatherRepository.removeFavoriteLocation(favoriteLocation)
//    }

}
