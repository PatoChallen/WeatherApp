package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.patofernandez.weatherapp.geocoding.model.ReverseGeocodingApiResponse

class GeocodingViewModel : ViewModel() {

    private val geocodingRepository = GeocodingRepository.getInstance()
    private var reverseGeocodingApiResponseData = geocodingRepository.getReverseGeocoding()

    fun getReverseGeocoding(): LiveData<ReverseGeocodingApiResponse> {
        return reverseGeocodingApiResponseData
    }

    fun setCoordinates(lat: Double, lon: Double) {
        reverseGeocodingApiResponseData = geocodingRepository.getReverseGeocodingByLatLng(lat, lon)
        reverseGeocodingApiResponseData.value
    }

}
