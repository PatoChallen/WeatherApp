package com.patofernandez.weatherapp.services

import com.patofernandez.weatherapp.model.OpenWeatherApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("weather")
    fun getWeather(@Query("appid") appid: String): Call<OpenWeatherApiResponse>

    @GET("payment_methods/card_issuers")
    fun getWeatherByCoords(@Query("lat") lat: Long,
                           @Query("lon") lon: Long,
                           @Query("appid") appid: String): Call<OpenWeatherApiResponse>

}