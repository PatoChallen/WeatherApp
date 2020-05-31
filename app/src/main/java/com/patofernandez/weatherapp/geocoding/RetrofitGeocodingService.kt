package com.patofernandez.weatherapp.geocoding

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitGeocodingService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://geocode.xyz/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @JvmStatic
    fun <S> createService(serviceClass: Class<S>?): S {
        return retrofit.create(serviceClass)
    }

}