package com.patofernandez.weatherapp.ui

import android.content.Context
import android.util.DisplayMetrics
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.patofernandez.weatherapp.databinding.FavoriteLocationItemBinding
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.databinding.WeatherForecastItemBinding
import com.patofernandez.weatherapp.databinding.WeatherHourItemBinding
import com.patofernandez.weatherapp.ui.common.DataBoundListAdapter
import com.patofernandez.weatherapp.vo.City
import com.patofernandez.weatherapp.vo.CityDay
import com.patofernandez.weatherapp.vo.DayHour
import com.patofernandez.weatherapp.vo.FavoriteLocation

/**
 * A RecyclerView adapter for [CityDay] class.
 */
class WeatherDaysAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val repoClickCallback: ((CityDay) -> Unit)?
) : DataBoundListAdapter<CityDay, WeatherForecastItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<CityDay>() {
        override fun areItemsTheSame(oldItem: CityDay, newItem: CityDay): Boolean {
            return oldItem.date == newItem.date
        }
        override fun areContentsTheSame(oldItem: CityDay, newItem: CityDay): Boolean {
            return oldItem.day == newItem.day
        }
    }
) {

    override fun createBinding(parent: ViewGroup): WeatherForecastItemBinding {
        val binding: WeatherForecastItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.weather_forecast_item,
            parent,
            false,
            dataBindingComponent
        )
        val layoutParams = binding.root.layoutParams
        val wm = parent.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        layoutParams.width = dm.widthPixels / 6
        binding.root.setOnClickListener {
            binding.day.let {
                repoClickCallback?.invoke(it!!)
            }
        }
        return binding
    }

    override fun bind(binding: WeatherForecastItemBinding, item: CityDay) {
        binding.day = item
    }

}
