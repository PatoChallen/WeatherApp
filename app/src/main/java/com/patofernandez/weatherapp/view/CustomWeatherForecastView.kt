package com.patofernandez.weatherapp.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.adapters.WeatherForecastAdapter
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.model.WeatherForecastApiResponse
import com.patofernandez.weatherapp.utils.FormatUtils
import kotlinx.android.synthetic.main.custom_weather_forecast.view.*

class CustomWeatherForecastView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.custom_weather_forecast, this)
    }

    fun setWeatherForecastData(data: WeatherForecastApiResponse, listener: OnForecastClickListener) {
        val formatedData = data.list.distinctBy { FormatUtils.formatedDay(it.date) }.map { Gson().fromJson(Gson().toJson(it), CurrentWeatherApiResponse::class.java) }
        formatedData.forEach { item ->
            var tempMax = item.main!!.tempMax
            var tempMin = item.main!!.tempMin
            data.list.filter { FormatUtils.formatedDay(it.date) == FormatUtils.formatedDay(item.date) }.forEach {
                if (it.main!!.tempMax > tempMax) tempMax = it.main!!.tempMax
                if (it.main!!.tempMin < tempMin) tempMin = it.main!!.tempMin
            }
            item.main!!.tempMax = tempMax
            item.main!!.tempMin = tempMin
        }
        weatherForecast.adapter = WeatherForecastAdapter(formatedData, listener)
    }

    interface OnForecastClickListener {
        fun onForecastClick(currentWeatherApiResponse: CurrentWeatherApiResponse)
    }

    companion object {
    }

}