package com.patofernandez.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patofernandez.weatherapp.R
import kotlinx.android.synthetic.main.favorite_location_item.view.*
import kotlinx.android.synthetic.main.weather_forecast_item.view.*

class FavoriteLocationsAdapter(
    private var forecastHours: List<String>
) : RecyclerView.Adapter<FavoriteLocationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_location_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = forecastHours[position]
        holder.favTitle.text = item
//        item.weather.first()?.let { weather ->
//            Picasso
//                .get()
//                .load("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
//                .into(holder.imgWeather)
//        }
//        item.main?.let { main ->
//            holder.tempMin.text = FormatUtils.formatedKelvinToCelsius(main.tempMin)
//            holder.tempMax.text = FormatUtils.formatedKelvinToCelsius(main.tempMax)
//        }

    }

    override fun getItemCount(): Int = forecastHours.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val favTitle: TextView = mView.favTitle
    }
}
