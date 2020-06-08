package com.patofernandez.weatherapp.repository

import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.api.OpenWeatherService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val openWeatherService: OpenWeatherService
) {

}
