package com.patofernandez.weatherapp.ui

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.repository.WeatherRepository
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.utils.AbsentLiveData
import com.patofernandez.weatherapp.utils.FormatUtils
import com.patofernandez.weatherapp.vo.FavoriteLocation
import com.patofernandez.weatherapp.vo.Resource
import javax.inject.Inject

class WeatherViewModel  @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _myLatLng = MutableLiveData<LatLng?>()
    private val _latLng = MutableLiveData<LatLng?>()

    val latLng: LiveData<LatLng?> = _latLng
    val results: LiveData<Resource<List<FavoriteLocation>>> = weatherRepository.loadFavoriteLocations()

    val currentLocation: LiveData<Resource<FavoriteLocation>> = _myLatLng.switchMap { latLng ->
        if (latLng == null) {
            AbsentLiveData.create()
        } else {
            weatherRepository.getCurrentWeatherByLocation(latLng).map { resource ->
                Resource<FavoriteLocation>(
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
                            lat = it.coordinates?.latitude?.toString(),
                            lng = it.coordinates?.longitude?.toString(),
                            iconUrl = "https://openweathermap.org/img/wn/${it.weather.first()?.icon}@4x.png",
                            country = it.sys?.country
                        ).apply {
                            coordinates = "$lat, $lng"
                            this.latLng = latLng
                        }
                    }
                )
            }
        }
    }

    val selectedLocation: LiveData<Resource<FavoriteLocation>> = _latLng.switchMap { latLng ->
        if (latLng == null) {
            AbsentLiveData.create()
        } else {
            weatherRepository.getCurrentWeatherByLocation(latLng).map { resource ->
                Resource<FavoriteLocation>(
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
                            lat = it.coordinates?.latitude?.toString(),
                            lng = it.coordinates?.longitude?.toString(),
                            iconUrl = "https://openweathermap.org/img/wn/${it.weather.first()?.icon}@4x.png",
                            country = it.sys?.country
                        ).apply {
                            coordinates = "$lat, $lng"
                            this.latLng = latLng
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

    fun setLatLng(latLng: LatLng?) {
        if (_latLng.value != latLng) {
            _latLng.value = latLng
        }
    }

    fun setLatLng() {
        if (_latLng.value != _myLatLng.value) {
            _latLng.value = _myLatLng.value
        }
    }

    fun retry() {
        _myLatLng.value?.let {
            _myLatLng.value = it
        }
    }

    fun addSelectedLocationToFavorites() {
        weatherRepository.addSelectedLocationToFavorites(selectedLocation.value?.data!!)
    }

    fun removeLocationFromFavorites(favoriteLocation: FavoriteLocation) {
        weatherRepository.removeLocationFromFavorites(favoriteLocation)
    }


//    fun getWeatherForecast(): LiveData<WeatherForecastApiResponse> {
//        return weatherForecastApiResponse
//    }
//
//    fun getWheaterHoursByDay(date: Long): List<CurrentWeatherApiResponse> {
//        return weatherForecastApiResponse.value!!.list.filter { FormatUtils.formatedDay(it.date) == FormatUtils.formatedDay(date) }
//    }
//
//    fun getFavoriteLocations(): MutableLiveData<List<CurrentWeatherApiResponse>> {
//        return weatherRepository.getFavoriteLocations()
//    }
//
//    fun addSelectedLocationToFavorites() {
//        weatherRepository.addSelectedLocationToFavorites()
//    }
//
//    fun getSelectedLocation(): MutableLiveData<CurrentWeatherApiResponse> {
//        return weatherRepository.getSelectedLocation()
//    }
//
//    fun setCoordinates(lat: Double, lon: Double) {
//        weatherRepository.setCoordinates(lat, lon)
//    }
//
//    fun setCurrentWeather(currentWeather: CurrentWeatherApiResponse) {
//        currentWeatherApiResponseData.value = currentWeather
//        weatherForecastApiResponse = weatherRepository.getWeatherForecastByCoords(currentWeather.coordinates!!.latitude, currentWeather.coordinates!!.longitude)
//    }
//
//    fun removeFavoriteLocation(favoriteLocation: CurrentWeatherApiResponse) {
//        weatherRepository.removeFavoriteLocation(favoriteLocation)
//    }

}
