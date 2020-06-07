package com.patofernandez.weatherapp.ui

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.db.WeatherTypeConverters
import com.patofernandez.weatherapp.repository.WeatherRepository
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.utils.AbsentLiveData
import com.patofernandez.weatherapp.utils.FormatUtils
import com.patofernandez.weatherapp.vo.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherViewModel  @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _myLatLng = MutableLiveData<LatLng?>()
    private val _latLng = MutableLiveData<LatLng?>()
    private val _selectedDay = MutableLiveData<CityDay>()

    val results: LiveData<Resource<List<FavoriteLocation>>> = weatherRepository.loadFavoriteLocations()

    val selectedDay: LiveData<CityDay> = _selectedDay

    val currentWeatherForecast: LiveData<Resource<City>> = _latLng.switchMap { latLng ->
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

//    val wheaterHoursByDay: LiveData<Resource<List<FavoriteLocation>>> = _date.switchMap { date ->
//        if (date == null) {
//            AbsentLiveData.create()
//        } else {
//            currentWeatherForecast.map { resource ->
//                Resource<List<FavoriteLocation>>(
//                    status = resource.status,
//                    message = resource.message,
//                    data = resource.data!!.list
//                        .filter { FormatUtils.formatedDay(it.date) == FormatUtils.formatedDay(date) }
//                        .map{
//                            FavoriteLocation(
//                                id = null,
//                                name = it.name,
//                                temp = it.main?.temp?.let {
//                                    FormatUtils.formatedKelvinToCelsius(
//                                        it
//                                    )
//                                },
//                                tempMax = it.main?.tempMax?.let {
//                                    FormatUtils.formatedKelvinToCelsius(
//                                        it
//                                    )
//                                },
//                                tempMin = it.main?.tempMin?.let {
//                                    FormatUtils.formatedKelvinToCelsius(
//                                        it
//                                    )
//                                },
//                                lat = it.coordinates?.latitude!!,
//                                lng = it.coordinates?.longitude!!,
//                                iconUrl = "https://openweathermap.org/img/wn/${it.weather.first()?.icon}@4x.png",
//                                country = it.sys?.country
//                            ).apply {
//                                coordinates = "$lat, $lng"
//                            }
//                        }
//                )
//            }
//        }
//    }


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
            _latLng.value?.let {
                _latLng.value = it
            }
        }
    }

    fun retry() {
        _latLng.value?.let {
           _latLng.value = it
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
