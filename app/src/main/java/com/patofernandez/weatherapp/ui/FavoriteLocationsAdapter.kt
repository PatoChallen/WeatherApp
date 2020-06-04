package com.patofernandez.weatherapp.ui

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.patofernandez.weatherapp.databinding.FavoriteLocationItemBinding
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.ui.common.DataBoundListAdapter
import com.patofernandez.weatherapp.vo.CurrentWeatherApiResponse

/**
 * A RecyclerView adapter for [CurrentWeatherApiResponse] class.
 */
class FavoriteLocationsAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val showFullName: Boolean,
    private val repoClickCallback: ((CurrentWeatherApiResponse) -> Unit)?
) : DataBoundListAdapter<CurrentWeatherApiResponse, FavoriteLocationItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<CurrentWeatherApiResponse>() {
        override fun areItemsTheSame(oldItem: CurrentWeatherApiResponse, newItem: CurrentWeatherApiResponse): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CurrentWeatherApiResponse, newItem: CurrentWeatherApiResponse): Boolean {
            return false // oldItem.description == newItem.description && oldItem.stars == newItem.stars
        }
    }
) {

    override fun createBinding(parent: ViewGroup): FavoriteLocationItemBinding {
        val binding = DataBindingUtil.inflate<FavoriteLocationItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.favorite_location_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.showFullName = showFullName
        binding.root.setOnClickListener {
            binding.repo?.let {
                repoClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: FavoriteLocationItemBinding, item: CurrentWeatherApiResponse) {
        binding.repo = item
    }
}
