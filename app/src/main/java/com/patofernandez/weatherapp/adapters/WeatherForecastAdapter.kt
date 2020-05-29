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
import com.patofernandez.weatherapp.view.CustomWeatherForecastView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_forecast_item.view.*

class WeatherForecastAdapter(
    private var forecast: List<CurrentWeatherApiResponse>,
    private val mListener: CustomWeatherForecastView.OnForecastClickListener?
) : RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as CurrentWeatherApiResponse
            mListener?.onForecastClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_forecast_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = forecast[position]
        holder.date.text = Utils.formatedDay(item.dt)
        item.weather.first()?.let { weather ->
            Picasso
                .get()
                .load("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
                .into(holder.imgWeather)
            holder.weather.text = weather.description
        }
        item.main?.let { main ->
            holder.tempMin.text = Utils.formatedKelvinToCelsius(main.temp_min)
            holder.tempMax.text = Utils.formatedKelvinToCelsius(main.temp_max)
            holder.humidity.text = main.humidity.toString().plus(" %")
            holder.pressure.text = main.pressure.toString().plus(" hpa")
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = forecast.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val date: TextView = mView.date
        val imgWeather: ImageView = mView.imgWeather
        val weather: TextView = mView.weather
        val tempMin: TextView = mView.tempMin
        val tempMax: TextView = mView.tempMax
        val humidity: TextView = mView.humidity
        val pressure: TextView = mView.pressure

    }
}
