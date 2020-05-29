package com.patofernandez.weatherapp.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.adapters.WeatherForecastAdapter
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.utils.Utils
import kotlinx.android.synthetic.main.custom_weather_forecast.view.*

class CustomWeatherForecastView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.custom_weather_forecast, this)
    }

    fun setWeatherForecastData(data: WeatherForecastApiResponse, listener: OnForecastClickListener) {
        weatherForecast.adapter = WeatherForecastAdapter(data.list.distinctBy { Utils.formatedDay(it.dt) }, listener)
    }

    interface OnForecastClickListener {
        fun onForecastClick(currentWeatherApiResponse: CurrentWeatherApiResponse)
    }

    companion object {
    }

}