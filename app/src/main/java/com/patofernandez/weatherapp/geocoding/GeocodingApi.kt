package com.patofernandez.weatherapp.geocoding

import com.patofernandez.weatherapp.geocoding.model.ReverseGeocodingApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GeocodingApi {

    @GET("/{lat},{lng}")
    fun reverseGeocoding(@Path("lat") lat: Double,
                         @Path("lng") lon: Double,
                         @Query("geoit") lang: String): Call<ReverseGeocodingApiResponse>

}