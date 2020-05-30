package com.patofernandez.weatherapp.adapters

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
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

    private var indexSelected: Int = 0
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.weather_forecast_item, parent, false)
        val layoutParams = view.layoutParams
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        layoutParams.width = dm.widthPixels / forecast.size

        return ViewHolder(view)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = forecast[position]
        holder.date.text = Utils.formatedDay(item.dt)
        if (indexSelected == position) {
            holder.card.setCardBackgroundColor(context.resources.getColor(R.color.cardActive))
        } else {
            holder.card.setCardBackgroundColor(context.resources.getColor(R.color.cardInactive))
        }
        item.weather.first()?.let { weather ->
            Picasso
                .get()
                .load("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
                .into(holder.imgWeather)
        }
        item.main?.let { main ->
            holder.tempMin.text = Utils.formatedKelvinToCelsius(main.temp_min)
            holder.tempMax.text = Utils.formatedKelvinToCelsius(main.temp_max)
            holder.humidity.text = main.humidity.toString().plus(" %")
            holder.pressure.text = main.pressure.toString().plus(" hpa")
        }

        with(holder.mView) {
            tag = item
            setOnClickListener{
                indexSelected = position
                mListener?.onForecastClick(item)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = forecast.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val date: TextView = mView.date
        val imgWeather: ImageView = mView.imgWeather
        val tempMin: TextView = mView.tempMin
        val tempMax: TextView = mView.tempMax
        val humidity: TextView = mView.humidity
        val pressure: TextView = mView.pressure
        val card: CardView = mView.card

    }
}
