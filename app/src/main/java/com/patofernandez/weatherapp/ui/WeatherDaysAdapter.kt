package com.patofernandez.weatherapp.ui

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.databinding.DayItemBinding
import com.patofernandez.weatherapp.ui.common.DataBoundListAdapter
import com.patofernandez.weatherapp.vo.CityDay

/**
 * A RecyclerView adapter for [CityDay] class.
 */
class WeatherDaysAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val repoClickCallback: ((CityDay) -> Unit)?
) : DataBoundListAdapter<CityDay, DayItemBinding>(
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
    var selectedItem: CityDay? = null

    override fun createBinding(parent: ViewGroup): DayItemBinding {
        val binding: DayItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.day_item,
            parent,
            false,
            dataBindingComponent
        )
        val wm = parent.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        with(binding.root) {
            layoutParams.width = dm.widthPixels / itemCount
            setOnClickListener {
                binding.day.let {
                    repoClickCallback?.invoke(it!!)
                    selectedItem = it
                    notifyDataSetChanged()
                }
            }
        }
        return binding
    }

    override fun bind(binding: DayItemBinding, item: CityDay) {
        binding.selected = if (item == selectedItem) R.color.cardActive else R.color.cardInactive
        binding.day = item
    }

}
