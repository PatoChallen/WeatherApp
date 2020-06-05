package com.patofernandez.weatherapp.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.patofernandez.weatherapp.vo.FavoriteLocation

/**
 * Main database description.
 */
@Database(
    entities = [
        FavoriteLocation::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDb : RoomDatabase() {

    abstract fun favoriteLocationDao(): FavoriteLocationDao

}
