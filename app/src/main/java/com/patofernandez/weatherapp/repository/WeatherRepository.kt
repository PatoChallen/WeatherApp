package com.patofernandez.weatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.api.*
import com.patofernandez.weatherapp.db.FavoriteLocationDao
import com.patofernandez.weatherapp.db.WeatherDb
import com.patofernandez.weatherapp.db.WeatherTypeConverters
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.vo.FavoriteLocation
import com.patofernandez.weatherapp.vo.LocationWeather
import com.patofernandez.weatherapp.vo.Resource
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: WeatherDb,
    private val favoriteLocationDao: FavoriteLocationDao,
    private val openWeatherService: OpenWeatherService
) {
    private val result = MediatorLiveData<Resource<List<FavoriteLocation>>>()

    fun loadFavoriteLocations(): LiveData<Resource<List<FavoriteLocation>>> {
        result.value = Resource.loading(null)
        val dbSource = favoriteLocationDao.load()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            result.value = Resource.success(data)
        }
        return result
    }

    fun loadCurrentLocation(latLng: LatLng): LiveData<Resource<LocationWeather>> {
        val lang = Locale.getDefault().language
        return object : NetworkBoundResource<LocationWeather, WeatherForecastApiResponse>(appExecutors) {
            override fun saveCallResult(item: WeatherForecastApiResponse) {
                favoriteLocationDao.insertCurrentLocation(
                    WeatherTypeConverters.forecastResponseToLocationWeather(item)
                )
            }
            override fun shouldFetch(data: LocationWeather?): Boolean = true

            override fun loadFromDb(): LiveData<LocationWeather> = favoriteLocationDao.loadLastCurrentLocation()

            override fun createCall(): LiveData<ApiResponse<WeatherForecastApiResponse>> =
                openWeatherService.getWeatherForecastByCoords(latLng.latitude, latLng.longitude, lang, KEY)
        }.asLiveData()
    }

    fun getCurrentWeatherByLocation(latLng: LatLng): LiveData<Resource<CurrentWeatherApiResponse>> {
        val result = MediatorLiveData<Resource<CurrentWeatherApiResponse>>()
        val lang = Locale.getDefault().language
        result.value = Resource.loading(null)
        val apiResponse = openWeatherService.getCurrentWeatherByCoords(
            lat = latLng.latitude,
            lon = latLng.longitude,
            lang = lang,
            appid = KEY
        )
        result.addSource(apiResponse) {  response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    result.value = Resource.success(response.body)
                }
                is ApiEmptyResponse -> {
                    result.value = Resource.success(null)
                }
                is ApiErrorResponse -> {
                    result.value = Resource.error(response.errorMessage, null)
                }
            }
        }
        return result
    }

    fun getForecastWeatherByLocation(latLng: LatLng?): LiveData<Resource<WeatherForecastApiResponse>> {
        val result = MediatorLiveData<Resource<WeatherForecastApiResponse>>()
        val lang = Locale.getDefault().language
        result.value = Resource.loading(null)
        latLng?.let {
            val apiResponse = openWeatherService.getWeatherForecastByCoords(
                lat = latLng.latitude,
                lon = latLng.longitude,
                lang = lang,
                appid = KEY
            )
            result.addSource(apiResponse) { response ->
                result.removeSource(apiResponse)
                when (response) {
                    is ApiSuccessResponse -> {
                        result.value = Resource.success(response.body)
                    }
                    is ApiEmptyResponse -> {
                        result.value = Resource.success(null)
                    }
                    is ApiErrorResponse -> {
                        result.value = Resource.error(response.errorMessage, null)
                    }
                }
            }
        }
        return result
    }

    fun addSelectedLocationToFavorites(favoriteLocation: FavoriteLocation) {
        appExecutors.diskIO().execute {
            db.runInTransaction{
                favoriteLocationDao.insert(favoriteLocation)
                appExecutors.mainThread().execute {
                    loadFavoriteLocations()
                }
            }
        }
    }

    fun removeLocationFromFavorites(favoriteLocation: FavoriteLocation) {
        appExecutors.diskIO().execute {
            db.runInTransaction{
                favoriteLocationDao.delete(favoriteLocation)
                appExecutors.mainThread().execute {
                    loadFavoriteLocations()
                }
            }
        }
    }

    companion object {
        const val TAG = "WeatherRepository"
        const val KEY = "4aacfcc02cb3fed3918e88987aaf6fc3"
    }

}
