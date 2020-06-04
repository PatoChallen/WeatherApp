package com.patofernandez.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.patofernandez.weatherapp.repository.LocationRepository
import com.patofernandez.weatherapp.utils.AbsentLiveData
import com.patofernandez.weatherapp.vo.*
import javax.inject.Inject

//@Target(AnnotationTarget.ANNOTATION_CLASS)
//@Target(AnnotationTarget.CLASS)
class LocationViewModel @Inject constructor(repository: LocationRepository) : ViewModel() {

    private val _location: MutableLiveData<LatLng> = MutableLiveData()

//    val location: LiveData<Resource<CurrentWeatherApiResponse>> =
//        Transformations.switchMap(_location) { input ->
//            repository.loadRepo(owner, name)
//    }
//    val contributors: LiveData<Resource<List<WeatherForecastApiResponse>>> =
//    Transformations.switchMap(_repoId) { input ->
//        input.ifExists { owner, name ->
//            repository.loadContributors(owner, name)
//        }
//    }

//    fun retry() {
//        val owner = _repoId.value?.owner
//        val name = _repoId.value?.name
//        if (owner != null && name != null) {
//            _repoId.value = RepoId(owner, name)
//        }
//    }
//
//    fun setId(owner: String, name: String) {
//        val update = RepoId(owner, name)
//        if (_repoId.value == update) {
//            return
//        }
//        _repoId.value = update
//    }

}
