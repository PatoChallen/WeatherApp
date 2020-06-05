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
import com.patofernandez.weatherapp.vo.FavoriteLocation

/**
 * A RecyclerView adapter for [FavoriteLocation] class.
 */
class FavoriteLocationsAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val repoClickCallback: ((FavoriteLocation) -> Unit)?
) : DataBoundListAdapter<FavoriteLocation, FavoriteLocationItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<FavoriteLocation>() {
        override fun areItemsTheSame(oldItem: FavoriteLocation, newItem: FavoriteLocation): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FavoriteLocation, newItem: FavoriteLocation): Boolean {
            return oldItem.coordinates == newItem.coordinates
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
        binding.root.setOnClickListener {
            binding.favoriteLocation?.let {
                repoClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: FavoriteLocationItemBinding, item: FavoriteLocation) {
        binding.favoriteLocation = item
    }
}
