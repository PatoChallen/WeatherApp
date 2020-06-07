package com.patofernandez.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.patofernandez.weatherapp.vo.FavoriteLocation
import com.patofernandez.weatherapp.vo.LocationWeather

@Dao
interface FavoriteLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteLocation: FavoriteLocation): Long

    @Query("SELECT * FROM favoriteLocation WHERE id = :id")
    fun findById(id: String): LiveData<FavoriteLocation>

    @Query("SELECT * FROM favoritelocation")
    fun load(): LiveData<List<FavoriteLocation>>

    @Delete
    fun delete(favoriteLocation: FavoriteLocation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentLocation(locationWeather: LocationWeather): Long

    @Query("SELECT * FROM locationweather")
    fun loadLastCurrentLocation(): LiveData<LocationWeather>
}
