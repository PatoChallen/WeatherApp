package com.patofernandez.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.binding.FragmentDataBindingComponent
import com.patofernandez.weatherapp.databinding.LocationWeatherFragmentBinding
import com.patofernandez.weatherapp.di.Injectable
import com.patofernandez.weatherapp.utils.autoCleared
import com.patofernandez.weatherapp.vo.Status
import javax.inject.Inject

class LocationWeatherFragment : Fragment(), OnMapReadyCallback, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var mMap: GoogleMap

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<LocationWeatherFragmentBinding>()

    var adapter by autoCleared<WeatherHoursAdapter>()

    var daysAdapter by autoCleared<WeatherDaysAdapter>()

    private val weatherViewModel: WeatherViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.location_weather_fragment,
            container,
            false,
            dataBindingComponent
        )
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        val weatherHoursAdapter = WeatherHoursAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        )
        binding.weatherHours.adapter = weatherHoursAdapter
        adapter = weatherHoursAdapter
        val weatherDaysAdapter = WeatherDaysAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ){
            weatherViewModel.setSelectedDay(it)
        }
        binding.customForecast.adapter = weatherDaysAdapter
        daysAdapter = weatherDaysAdapter
    }

    private fun initRecyclerView() {
        binding.result = weatherViewModel.currentWeatherForecast
        weatherViewModel.selectedDay.observe(viewLifecycleOwner, Observer { cityDay ->
            binding.day = cityDay
            daysAdapter.selectedItem = cityDay
            adapter.submitList(cityDay.hours)
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e(TAG, "onMapReady")
        mMap = googleMap
        mMap.uiSettings.apply {
            isScrollGesturesEnabled = false
            isZoomGesturesEnabled = false
        }
        weatherViewModel.currentWeatherForecast.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == Status.SUCCESS){
                result.data?.let { city ->
                    binding.location = result.data
                    daysAdapter.submitList(result.data!!.days)
                    weatherViewModel.setSelectedDay(result.data.days.first())
                    updateMap(city.getLatLng(), city.city)
                }
            }
        })
    }

    private fun updateMap(latLng: LatLng, title: String) {
        Log.e(TAG, "updateMap")
        mMap.apply {
            addMarker(MarkerOptions().position(latLng).title(title))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT))
        }
    }

    companion object {
        const val TAG = "LocationWeatherFragment"
        const val ZOOM_DEFAULT = 12F
    }

}
