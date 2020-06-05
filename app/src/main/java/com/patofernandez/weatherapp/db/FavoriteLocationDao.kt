package com.patofernandez.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.patofernandez.weatherapp.vo.FavoriteLocation

/**
 * Interface for database access for User related operations.
 */
@Dao
interface FavoriteLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteLocation: FavoriteLocation): Long

    @Query("SELECT * FROM favoriteLocation WHERE id = :id")
    fun findById(id: String): LiveData<FavoriteLocation>

    @Query("SELECT * FROM favoritelocation")
    fun load(): LiveData<List<FavoriteLocation>>
}
