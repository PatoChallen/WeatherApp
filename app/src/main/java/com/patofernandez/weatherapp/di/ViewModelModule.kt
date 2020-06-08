package com.patofernandez.weatherapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.patofernandez.weatherapp.ui.WeatherViewModel
import com.patofernandez.weatherapp.viewmodel.LocationViewModel
import com.patofernandez.weatherapp.viewmodel.WeathweAppModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun bindWeatherViewModel(weatherViewModel: WeatherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel::class)
    abstract fun bindLocationViewModel(locationViewModel: LocationViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: WeathweAppModelFactory): ViewModelProvider.Factory
}
