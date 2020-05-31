package com.patofernandez.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.services.RetrofitService.createService
import com.patofernandez.weatherapp.services.OpenWeatherApi
import com.patofernandez.weatherapp.utils.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class WeatherRepository {

    private val openWeatherApi: OpenWeatherApi = createService(
        OpenWeatherApi::class.java
    )

    fun getCurrentWeatherByCoords(lat: Double, lon: Double): MutableLiveData<CurrentWeatherApiResponse> {
        val currentWeatherApiResponseData = MutableLiveData<CurrentWeatherApiResponse>()
        val lang = Locale.getDefault().language
        openWeatherApi.getCurrentWeatherByCoords(lat, lon, lang, KEY).enqueue(object : Callback<CurrentWeatherApiResponse>{
            override fun onResponse(call: Call<CurrentWeatherApiResponse>, response: Response<CurrentWeatherApiResponse>) {
                currentWeatherApiResponseData.value = response.body()
            }
            override fun onFailure(call: Call<CurrentWeatherApiResponse>, t: Throwable) {
                Log.e(TAG, t.message)
                currentWeatherApiResponseData.value = null
            }
        })
        return currentWeatherApiResponseData
    }

    fun getWeatherForecastByCoords(lat: Double, lon: Double): MutableLiveData<WeatherForecastApiResponse> {
        val weatherForecastApiResponseData = MutableLiveData<WeatherForecastApiResponse>()
        val lang = Locale.getDefault().language
        openWeatherApi.getWeatherForecastByCoords(lat, lon, lang, KEY).enqueue(object : Callback<WeatherForecastApiResponse>{
            override fun onResponse(call: Call<WeatherForecastApiResponse>, response: Response<WeatherForecastApiResponse>) {
                weatherForecastApiResponseData.value = response.body()
            }
            override fun onFailure(call: Call<WeatherForecastApiResponse>, t: Throwable) {
                Log.e(TAG, t.message)
                weatherForecastApiResponseData.value = null
            }
        })
        return weatherForecastApiResponseData
    }

    fun getFavoriteLocations(): List<LatLng> {
        return Preferences.favoriteLocations.getFavoriteLocations()
    }

    companion object {
        private var weatherRepository: WeatherRepository? = null
        fun getInstance(): WeatherRepository {
            if (weatherRepository == null) {
                weatherRepository = WeatherRepository()
            }
            return weatherRepository!!
        }

        const val TAG = "WeatherRepository"
        const val KEY = "4aacfcc02cb3fed3918e88987aaf6fc3"
    }

}