package com.patofernandez.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.vo.CurrentWeatherApiResponse
import kotlinx.android.synthetic.main.favorite_location_item.view.*

class FavoriteLocationsAdapter(
    private var forecastHours: List<CurrentWeatherApiResponse>,
    private val mListener: OnFavoriteActionListener?
) : RecyclerView.Adapter<FavoriteLocationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_location_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = forecastHours[position]
        holder.favTitle.text = item.name
//        item.weather.first()?.let { weather ->
//            Picasso
//                .get()
//                .load("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
//                .into(holder.imgWeather)
//        }
        holder.favDelete.setOnClickListener {
            mListener?.onFavoriteLocationDelete(item)
        }

        holder.mView.setOnClickListener {
            mListener?.onFavoriteLocationClick(item)
        }
    }

    override fun getItemCount(): Int = forecastHours.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val favTitle: TextView = mView.favTitle
        val favDelete: ImageView = mView.delete
    }
    interface OnFavoriteActionListener {
        fun onFavoriteLocationClick(favoriteLocation: CurrentWeatherApiResponse)
        fun onFavoriteLocationDelete(favoriteLocation: CurrentWeatherApiResponse)
    }
}
