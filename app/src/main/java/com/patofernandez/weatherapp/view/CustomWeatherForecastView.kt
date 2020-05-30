package com.patofernandez.weatherapp.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
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
        val formatedData = data.list.distinctBy { Utils.formatedDay(it.dt) }.map { Gson().fromJson(Gson().toJson(it), CurrentWeatherApiResponse::class.java) }
        formatedData.forEach { item ->
            var tempMax = item.main!!.temp_max
            var tempMin = item.main!!.temp_min
            data.list.filter { Utils.formatedDay(it.dt) == Utils.formatedDay(item.dt) }.forEach {
                if (it.main!!.temp_max > tempMax) tempMax = it.main!!.temp_max
                if (it.main!!.temp_min < tempMin) tempMin = it.main!!.temp_min
            }
            item.main!!.temp_max = tempMax
            item.main!!.temp_min = tempMin
        }
        weatherForecast.adapter = WeatherForecastAdapter(formatedData, listener)
    }

    interface OnForecastClickListener {
        fun onForecastClick(currentWeatherApiResponse: CurrentWeatherApiResponse)
    }

    companion object {
    }

}