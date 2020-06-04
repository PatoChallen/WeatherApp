package com.patofernandez.weatherapp.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.api.OpenWeatherService
import com.patofernandez.weatherapp.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Repo instances.
 *
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
//@OpenForTesting
class LocationRepository @Inject constructor(
    private val appExecutors: AppExecutors,
//    private val db: GithubDb,
//    private val repoDao: RepoDao,
    private val openWeatherService: OpenWeatherService
) {

//    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)
//
//    fun loadRepos(owner: String): LiveData<Resource<List<Repo>>> {
//        return object : NetworkBoundResource<List<Repo>, List<Repo>>(appExecutors) {
//            override fun saveCallResult(item: List<Repo>) {
//                repoDao.insertRepos(item)
//            }
//
//            override fun shouldFetch(data: List<Repo>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(owner)
//            }
//
//            override fun loadFromDb() = repoDao.loadRepositories(owner)
//
//            override fun createCall() = githubService.getRepos(owner)
//
//            override fun onFetchFailed() {
//                repoListRateLimit.reset(owner)
//            }
//        }.asLiveData()
//    }
//
//    fun loadRepo(owner: String, name: String): LiveData<Resource<LatLng>> {
//        return object : NetworkBoundResource<LatLng, Repo>(appExecutors) {
//            override fun saveCallResult(item: LatLng) {
//                repoDao.insert(item)
//            }
//
//            override fun shouldFetch(data: LatLng?) = data == null
//
//            override fun loadFromDb() = repoDao.load(
//                ownerLogin = owner,
//                name = name
//            )
//
//            override fun createCall() = openWeatherService.ge(
//                owner = owner,
//                name = name
//            )
//        }.asLiveData()
//    }
//
//    fun loadContributors(owner: String, name: String): LiveData<Resource<List<Contributor>>> {
//        return object : NetworkBoundResource<List<Contributor>, List<Contributor>>(appExecutors) {
//            override fun saveCallResult(item: List<Contributor>) {
//                item.forEach {
//                    it.repoName = name
//                    it.repoOwner = owner
//                }
//                db.runInTransaction {
//                    repoDao.createRepoIfNotExists(
//                        Repo(
//                            id = Repo.UNKNOWN_ID,
//                            name = name,
//                            fullName = "$owner/$name",
//                            description = "",
//                            owner = Repo.Owner(owner, null),
//                            stars = 0
//                        )
//                    )
//                    repoDao.insertContributors(item)
//                }
//            }
//
//            override fun shouldFetch(data: List<Contributor>?): Boolean {
//                return data == null || data.isEmpty()
//            }
//
//            override fun loadFromDb() = repoDao.loadContributors(owner, name)
//
//            override fun createCall() = githubService.getContributors(owner, name)
//        }.asLiveData()
//    }
//
//    fun searchNextPage(query: String): LiveData<Resource<Boolean>> {
//        val fetchNextSearchPageTask = FetchNextSearchPageTask(
//            query = query,
//            githubService = githubService,
//            db = db
//        )
//        appExecutors.networkIO().execute(fetchNextSearchPageTask)
//        return fetchNextSearchPageTask.liveData
//    }
//
//    fun search(query: String): LiveData<Resource<List<Repo>>> {
//        return object : NetworkBoundResource<List<Repo>, RepoSearchResponse>(appExecutors) {
//
//            override fun saveCallResult(item: RepoSearchResponse) {
//                val repoIds = item.items.map { it.id }
//                val repoSearchResult = RepoSearchResult(
//                    query = query,
//                    repoIds = repoIds,
//                    totalCount = item.total,
//                    next = item.nextPage
//                )
//                db.runInTransaction {
//                    repoDao.insertRepos(item.items)
//                    repoDao.insert(repoSearchResult)
//                }
//            }
//
//            override fun shouldFetch(data: List<Repo>?) = data == null
//
//            override fun loadFromDb(): LiveData<List<Repo>> {
//                return repoDao.search(query).switchMap { searchData ->
//                    if (searchData == null) {
//                        AbsentLiveData.create()
//                    } else {
//                        repoDao.loadOrdered(searchData.repoIds)
//                    }
//                }
//            }
//
//            override fun createCall() = githubService.searchRepos(query)
//
//            override fun processResponse(response: ApiSuccessResponse<RepoSearchResponse>)
//                    : RepoSearchResponse {
//                val body = response.body
//                body.nextPage = response.nextPage
//                return body
//            }
//        }.asLiveData()
//    }
}