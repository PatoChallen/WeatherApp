package com.patofernandez.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.api.OpenWeatherService
import com.patofernandez.weatherapp.services.RetrofitService.createService
import com.patofernandez.weatherapp.utils.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

//@Singleton
//@OpenForTesting
class WeatherRepository
//@Inject constructor(
//    private val appExecutors: AppExecutors,
//    private val db: GithubDb,
//    private val repoDao: RepoDao,
//    private val openWeatherService: OpenWeatherService
//)
{

    private val favoriteLocations = MutableLiveData<List<CurrentWeatherApiResponse>>()
    private val selectedLocation = MutableLiveData<CurrentWeatherApiResponse>()
    private val openWeatherService: OpenWeatherService = createService(
        OpenWeatherService::class.java
    )

    fun getSelectedLocation(): MutableLiveData<CurrentWeatherApiResponse> {
        return selectedLocation
    }

    fun setCoordinates(lat: Double, lon: Double) {
        val lang = Locale.getDefault().language
        openWeatherService.getCurrentWeatherByCoords(lat, lon, lang, KEY).enqueue(object : Callback<CurrentWeatherApiResponse>{
            override fun onResponse(call: Call<CurrentWeatherApiResponse>, response: Response<CurrentWeatherApiResponse>) {
                selectedLocation.value = response.body()
            }
            override fun onFailure(call: Call<CurrentWeatherApiResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                selectedLocation.value = null
            }
        })
    }

    private fun getCurrentWeatherByCoords(lat: Double, lon: Double): MutableLiveData<CurrentWeatherApiResponse> {
        val currentWeatherApiResponseData = MutableLiveData<CurrentWeatherApiResponse>()
        val lang = Locale.getDefault().language
        openWeatherService.getCurrentWeatherByCoords(lat, lon, lang, KEY).enqueue(object : Callback<CurrentWeatherApiResponse>{
            override fun onResponse(call: Call<CurrentWeatherApiResponse>, response: Response<CurrentWeatherApiResponse>) {
                currentWeatherApiResponseData.value = response.body()
            }
            override fun onFailure(call: Call<CurrentWeatherApiResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                currentWeatherApiResponseData.value = null
            }
        })
        return currentWeatherApiResponseData
    }

    fun getWeatherForecastByCoords(lat: Double, lon: Double): MutableLiveData<WeatherForecastApiResponse> {
        val weatherForecastApiResponseData = MutableLiveData<WeatherForecastApiResponse>()
        val lang = Locale.getDefault().language
        openWeatherService.getWeatherForecastByCoords(lat, lon, lang, KEY).enqueue(object : Callback<WeatherForecastApiResponse>{
            override fun onResponse(call: Call<WeatherForecastApiResponse>, response: Response<WeatherForecastApiResponse>) {
                weatherForecastApiResponseData.value = response.body()
            }
            override fun onFailure(call: Call<WeatherForecastApiResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                weatherForecastApiResponseData.value = null
            }
        })
        return weatherForecastApiResponseData
    }

    fun getFavoriteLocations(): MutableLiveData<List<CurrentWeatherApiResponse>> {
        favoriteLocations.value = ArrayList()
        Preferences.favoriteLocations.getFavoriteLocations().forEach { latLng ->
            getCurrentWeatherByCoords(latLng.latitude, latLng.longitude).observeForever{
                val list = favoriteLocations.value!!.toMutableList()
                list.add(it)
                favoriteLocations.value = list
            }
        }
        return favoriteLocations
    }

    fun addSelectedLocationToFavorites() {
        selectedLocation.value!!.coordinates?.let {
            Preferences.favoriteLocations.addToFavorite(LatLng(it.latitude, it.longitude))
            getFavoriteLocations()
        }
    }

    fun removeFavoriteLocation(favoriteLocation: CurrentWeatherApiResponse) {
        favoriteLocation.coordinates?.let {
            Preferences.favoriteLocations.removeFromFavorites(LatLng(it.latitude, it.longitude))
            val updatedFavorites = favoriteLocations.value!!.toMutableList()
            updatedFavorites.remove(favoriteLocation)
            favoriteLocations.value = updatedFavorites
        }
    }

    companion object {
        private var weatherRepository: WeatherRepository? = null
        fun getInstance(): WeatherRepository {
            if (weatherRepository == null) {
                weatherRepository = WeatherRepository()
            }
            return weatherRepository!!
        }

        const val TAG = "WeatherRepository"
        const val KEY = "4aacfcc02cb3fed3918e88987aaf6fc3"
    }

}