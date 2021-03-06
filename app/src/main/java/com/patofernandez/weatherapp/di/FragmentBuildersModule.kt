package com.patofernandez.weatherapp.di

import com.patofernandez.weatherapp.ui.HomeFragment
import com.patofernandez.weatherapp.ui.LocationWeatherFragment
import com.patofernandez.weatherapp.ui.SelectLocationFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun locationWeatherFragment(): LocationWeatherFragment

    @ContributesAndroidInjector
    abstract fun selectLocationFragment(): SelectLocationFragment

    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment
}
