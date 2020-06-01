package com.patofernandez.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.patofernandez.weatherapp.geocoding.GeocodingApi
import com.patofernandez.weatherapp.geocoding.RetrofitGeocodingService.createService
import com.patofernandez.weatherapp.geocoding.model.ReverseGeocodingApiResponse
import com.patofernandez.weatherapp.utils.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeocodingRepository {

    private val reverseGeocodingApiResponseData = MutableLiveData<ReverseGeocodingApiResponse>()
    private val geocodingApi: GeocodingApi = createService(
        GeocodingApi::class.java
    )

    fun getReverseGeocoding(): MutableLiveData<ReverseGeocodingApiResponse> {
        return reverseGeocodingApiResponseData
    }

    fun getReverseGeocodingByLatLng(lat: Double, lng: Double): MutableLiveData<ReverseGeocodingApiResponse> {
        geocodingApi.reverseGeocoding(lat, lng, RESPONSE_FORMAT).enqueue(object : Callback<ReverseGeocodingApiResponse>{
            override fun onResponse(call: Call<ReverseGeocodingApiResponse>, response: Response<ReverseGeocodingApiResponse>) {
                reverseGeocodingApiResponseData.value = response.body()
            }
            override fun onFailure(call: Call<ReverseGeocodingApiResponse>, t: Throwable) {
                Log.e(TAG, t.message)
            }
        })
        return reverseGeocodingApiResponseData
    }

    companion object {
        private var geocodingRepository: GeocodingRepository? = null
        fun getInstance(): GeocodingRepository {
            if (geocodingRepository == null) {
                geocodingRepository = GeocodingRepository()
            }
            return geocodingRepository!!
        }

        const val TAG = "GeocodingRepository"
        const val RESPONSE_FORMAT = "json"
        const val AUTH = "202452702209908236304x6126"
    }

}