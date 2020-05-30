package com.patofernandez.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_forecast_item.view.imgWeather
import kotlinx.android.synthetic.main.weather_forecast_item.view.tempMax
import kotlinx.android.synthetic.main.weather_forecast_item.view.tempMin
import kotlinx.android.synthetic.main.weather_hour_item.view.*

class WeatherHoursAdapter(
    private var forecastHours: List<CurrentWeatherApiResponse>
) : RecyclerView.Adapter<WeatherHoursAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_hour_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = forecastHours[position]
        holder.date.text = Utils.formatedHour(item.dt)
        item.weather.first()?.let { weather ->
            Picasso
                .get()
                .load("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
                .into(holder.imgWeather)
        }
        item.main?.let { main ->
            holder.tempMin.text = Utils.formatedKelvinToCelsius(main.temp_min)
            holder.tempMax.text = Utils.formatedKelvinToCelsius(main.temp_max)
        }

    }

    override fun getItemCount(): Int = forecastHours.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val date: TextView = mView.day
        val imgWeather: ImageView = mView.imgWeather
        val tempMin: TextView = mView.tempMin
        val tempMax: TextView = mView.tempMax

    }
}
