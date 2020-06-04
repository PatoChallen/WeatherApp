package com.patofernandez.weatherapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.api.OpenWeatherService
import com.patofernandez.weatherapp.utils.Preferences
import com.patofernandez.weatherapp.viewmodel.WeatherRepository
import com.patofernandez.weatherapp.vo.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.vo.Resource
import com.patofernandez.weatherapp.vo.WeatherForecastApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Repository that handles Repo instances.
 *
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
//@OpenForTesting
class WeatherRepository @Inject constructor(
    private val appExecutors: AppExecutors,
//    private val db: GithubDb,
//    private val repoDao: RepoDao,
    private val openWeatherService: OpenWeatherService
) {

    fun getFavoriteLocations(): LiveData<Resource<List<CurrentWeatherApiResponse>>> {
        TODO("Not yet implemented")
    }

    fun loadFavoriteLocations(): LiveData<Resource<List<CurrentWeatherApiResponse>>> {
        return object : NetworkBoundResource<List<CurrentWeatherApiResponse>, List<CurrentWeatherApiResponse>>(appExecutors) {
            override fun saveCallResult(item: List<CurrentWeatherApiResponse>) {
                item.forEach {
                    it.repoName = name
                    it.repoOwner = owner
                }
                db.runInTransaction {
                    repoDao.createRepoIfNotExists(
                        Repo(
                            id = Repo.UNKNOWN_ID,
                            name = name,
                            fullName = "$owner/$name",
                            description = "",
                            owner = Repo.Owner(owner, null),
                            stars = 0
                        )
                    )
                    repoDao.insertContributors(item)
                }
            }

            override fun shouldFetch(data: List<CurrentWeatherApiResponse>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb() = repoDao.loadContributors(owner, name)

            override fun createCall() = githubService.getContributors(owner, name)
        }.asLiveData()
    }


    companion object {
        const val TAG = "WeatherRepository"
        const val KEY = "4aacfcc02cb3fed3918e88987aaf6fc3"
    }

}
