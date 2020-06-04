package com.patofernandez.weatherapp.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.patofernandez.weatherapp.db.RepoDao
import com.patofernandez.weatherapp.db.UserDao

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        Repo::class,
        Contributor::class,
        RepoSearchResult::class],
    version = 3,
    exportSchema = false
)
abstract class WeatherDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun repoDao(): RepoDao
}
