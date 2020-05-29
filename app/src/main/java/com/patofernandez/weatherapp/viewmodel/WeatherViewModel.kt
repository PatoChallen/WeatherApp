package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository.getInstance()

}
