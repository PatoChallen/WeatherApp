package com.patofernandez.weatherapp.application

import android.app.Application

class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        weatherApplication = this
    }

    companion object {
        private lateinit var weatherApplication: WeatherApplication
        fun getInstance(): WeatherApplication {
            return weatherApplication
        }
    }
}