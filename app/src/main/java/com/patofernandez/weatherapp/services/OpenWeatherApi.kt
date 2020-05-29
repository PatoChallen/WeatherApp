package com.patofernandez.weatherapp.services

import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("weather")
    fun getWeather(@Query("appid") appid: String): Call<CurrentWeatherApiResponse>

    @GET("weather")
    fun getCurrentWeatherByCoords(@Query("lat") lat: Double,
                                  @Query("lon") lon: Double,
                                  @Query("lang") lang: String,
                                  @Query("appid") appid: String): Call<CurrentWeatherApiResponse>

    @GET("forecast")
    fun getWeatherForecastByCoords(@Query("lat") lat: Double,
                           @Query("lon") lon: Double,
                           @Query("lang") lang: String,
                           @Query("appid") appid: String): Call<WeatherForecastApiResponse>

}