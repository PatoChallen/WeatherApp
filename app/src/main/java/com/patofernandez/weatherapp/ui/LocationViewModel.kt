package com.patofernandez.weatherapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.repository.LocationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationViewModel @Inject constructor(repository: LocationRepository) : ViewModel() {

    private val _location: MutableLiveData<LatLng> = MutableLiveData()

}
