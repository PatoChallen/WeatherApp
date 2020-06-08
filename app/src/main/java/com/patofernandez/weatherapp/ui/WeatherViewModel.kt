package com.patofernandez.weatherapp.ui

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.db.WeatherTypeConverters
import com.patofernandez.weatherapp.repository.WeatherRepository
import com.patofernandez.weatherapp.utils.AbsentLiveData
import com.patofernandez.weatherapp.utils.FormatUtils
import com.patofernandez.weatherapp.vo.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherViewModel  @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _myLatLng = MutableLiveData<LatLng?>()
    private val _selectedLatLng = MutableLiveData<LatLng?>()
    private val _forecastLatLng = MutableLiveData<LatLng?>()
    private val _selectedDay = MutableLiveData<CityDay>()

    val results: LiveData<Resource<List<FavoriteLocation>>> = weatherRepository.loadFavoriteLocations()

    val selectedDay: LiveData<CityDay> = _selectedDay

    val currentWeatherForecast: LiveData<Resource<City>> = _forecastLatLng.switchMap { latLng ->
        if (latLng == null) {
            AbsentLiveData.create()
        } else {
            weatherRepository.getForecastWeatherByLocation(latLng).map { resource ->
                Resource(
                    status = resource.status,
                    message = resource.message,
                    data = resource.data?.let {
                        WeatherTypeConverters.forecastResponseToCity(it)
                    }
                )
            }
        }
    }

    val currentLocation: LiveData<Resource<LocationWeather>> = _myLatLng.switchMap { latLng ->
        if (latLng == null) {
            AbsentLiveData.create()
        } else {
            weatherRepository.loadCurrentLocation(latLng)
        }
    }

    val selectedLocation: LiveData<Resource<FavoriteLocation>> = _selectedLatLng.switchMap { latLng ->
        if (latLng == null) {
            AbsentLiveData.create()
        } else {
            weatherRepository.getCurrentWeatherByLocation(latLng).map { resource ->
                Resource(
                    status = resource.status,
                    message = resource.message,
                    data = resource.data?.let {
                        FavoriteLocation(
                            id = null,
                            name = it.name,
                            temp = it.main?.temp?.let {
                                FormatUtils.formatedKelvinToCelsius(
                                    it
                                )
                            },
                            tempMax = it.main?.tempMax?.let {
                                FormatUtils.formatedKelvinToCelsius(
                                    it
                                )
                            },
                            tempMin = it.main?.tempMin?.let {
                                FormatUtils.formatedKelvinToCelsius(
                                    it
                                )
                            },
                            lat = it.coordinates?.latitude!!,
                            lng = it.coordinates?.longitude!!,
                            iconUrl = "https://openweathermap.org/img/wn/${it.weather.first()?.icon}@4x.png",
                            country = it.sys?.country
                        ).apply {
                            coordinates = "$lat, $lng"
                        }
                    }
                )
            }
        }
    }

    fun setMyLatLng(latLng: LatLng?) {
        if (_myLatLng.value != latLng) {
            _myLatLng.value = latLng
        }
    }

    fun setForecastLatLng(latLng: LatLng?) {
        if (_forecastLatLng.value != latLng) {
            _forecastLatLng.value = latLng
        }
    }

    fun setSelectedLatLng(latLng: LatLng?) {
        if (_selectedLatLng.value != latLng) {
            _selectedLatLng.value = latLng
        }
    }

    fun setForecastLatLng() {
        if (_forecastLatLng.value != _myLatLng.value) {
            _forecastLatLng.value = _myLatLng.value
        }
    }

    fun retry() {
        _selectedLatLng.value?.let {
           _selectedLatLng.value = it
        }
    }

    fun addSelectedLocationToFavorites() {
        weatherRepository.addSelectedLocationToFavorites(selectedLocation.value?.data!!)
    }

    fun removeLocationFromFavorites(favoriteLocation: FavoriteLocation) {
        weatherRepository.removeLocationFromFavorites(favoriteLocation)
    }

    fun setSelectedDay(cityDay: CityDay) {
        _selectedDay.value = cityDay
    }

}
