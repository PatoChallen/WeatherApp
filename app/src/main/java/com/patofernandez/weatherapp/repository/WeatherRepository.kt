package com.patofernandez.weatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.api.ApiEmptyResponse
import com.patofernandez.weatherapp.api.ApiErrorResponse
import com.patofernandez.weatherapp.api.ApiSuccessResponse
import com.patofernandez.weatherapp.api.OpenWeatherService
import com.patofernandez.weatherapp.db.FavoriteLocationDao
import com.patofernandez.weatherapp.db.WeatherDb
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.vo.FavoriteLocation
import com.patofernandez.weatherapp.vo.Resource
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Repo instances.
 *
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
//@OpenForTesting
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

    fun getCurrentWeatherByLocation(latLng: LatLng): LiveData<Resource<CurrentWeatherApiResponse>> {
        val result = MediatorLiveData<Resource<CurrentWeatherApiResponse>>()
        val lang = Locale.getDefault().language
        result.value = Resource.loading(null)
        val apiResponse = openWeatherService.getCurrentWeatherByCoords2(
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
