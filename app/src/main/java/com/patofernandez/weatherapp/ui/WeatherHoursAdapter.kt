package com.patofernandez.weatherapp.ui

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.patofernandez.weatherapp.databinding.FavoriteLocationItemBinding
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.databinding.WeatherHourItemBinding
import com.patofernandez.weatherapp.ui.common.DataBoundListAdapter
import com.patofernandez.weatherapp.vo.City
import com.patofernandez.weatherapp.vo.DayHour
import com.patofernandez.weatherapp.vo.FavoriteLocation

/**
 * A RecyclerView adapter for [DayHour] class.
 */
class WeatherHoursAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors
) : DataBoundListAdapter<DayHour, WeatherHourItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<DayHour>() {
        override fun areItemsTheSame(oldItem: DayHour, newItem: DayHour): Boolean {
            return oldItem.date == newItem.date
        }
        override fun areContentsTheSame(oldItem: DayHour, newItem: DayHour): Boolean {
            return oldItem.hour == newItem.hour
        }
    }
) {

    override fun createBinding(parent: ViewGroup): WeatherHourItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.weather_hour_item,
            parent,
            false,
            dataBindingComponent
        )
    }

    override fun bind(binding: WeatherHourItemBinding, item: DayHour) {
        binding.hour = item
    }

}
