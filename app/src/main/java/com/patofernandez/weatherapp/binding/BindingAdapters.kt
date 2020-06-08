package com.patofernandez.weatherapp.binding

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibility")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("background")
    fun setBackgroundResource(view: View, resource: Int) {
        view.setBackgroundResource(resource)
    }
}
