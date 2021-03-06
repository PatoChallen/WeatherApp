package com.patofernandez.weatherapp.api

import androidx.lifecycle.LiveData
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("weather")
    fun getCurrentWeatherByCoords(@Query("lat") lat: Double,
                                  @Query("lon") lon: Double,
                                  @Query("lang") lang: String,
                                  @Query("appid") appid: String): LiveData<ApiResponse<CurrentWeatherApiResponse>>

    @GET("forecast")
    fun getWeatherForecastByCoords(@Query("lat") lat: Double,
                                   @Query("lon") lon: Double,
                                   @Query("lang") lang: String,
                                   @Query("appid") appid: String):LiveData<ApiResponse<WeatherForecastApiResponse>>

}