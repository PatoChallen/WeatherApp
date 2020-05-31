package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
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

    fun getFavoriteLocations(): List<LatLng> {
        return geocodingRepository.getFavoriteLocations()
    }

    fun addFavoriteLocation(latLng: LatLng) {
        geocodingRepository.addFavoriteLocation(latLng)
    }

}
