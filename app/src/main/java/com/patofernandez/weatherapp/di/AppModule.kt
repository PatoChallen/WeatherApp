package com.patofernandez.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.patofernandez.weatherapp.api.OpenWeatherService
import com.patofernandez.weatherapp.db.FavoriteLocationDao
import com.patofernandez.weatherapp.db.WeatherDb
import com.patofernandez.weatherapp.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideWeatherService(): OpenWeatherService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(OpenWeatherService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): WeatherDb {
        return Room
            .databaseBuilder(app, WeatherDb::class.java, "weather.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideFavoriteLocationDao(db: WeatherDb): FavoriteLocationDao {
        return db.favoriteLocationDao()
    }

}
