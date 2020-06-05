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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.binding.FragmentDataBindingComponent
import com.patofernandez.weatherapp.databinding.SelectLocationFragmentBinding
import com.patofernandez.weatherapp.di.Injectable
import com.patofernandez.weatherapp.utils.autoCleared
import com.patofernandez.weatherapp.vo.FavoriteLocation
import javax.inject.Inject

class LocationWeatherFragment : Fragment(), OnMapReadyCallback, Injectable {

    private lateinit var mMap: GoogleMap

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<SelectLocationFragmentBinding>()

    var adapter by autoCleared<FavoriteLocationsAdapter>()

    val weatherViewModel: WeatherViewModel by viewModels {
        viewModelFactory
    }

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
        val rvAdapter = FavoriteLocationsAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors,
            onFavoriteActionListener = object : FavoriteLocationsAdapter.OnFavoriteActionListener {
                override fun onFavoriteLocationClick(favoriteLocation: FavoriteLocation) {
                    weatherViewModel.setLatLng(favoriteLocation.latLng)
                    findNavController().navigate(R.id.action_homeFragment_to_locationWeatherFragment)
                }

                override fun onFavoriteLocationDelete(favoriteLocation: FavoriteLocation) {
                    weatherViewModel.removeLocationFromFavorites(favoriteLocation)
                }
            }
        )

//        binding.favoriteLocations.adapter = rvAdapter
        adapter = rvAdapter

    }

    private fun initRecyclerView() {
//        binding.result = weatherViewModel.results
        weatherViewModel.results.observe(viewLifecycleOwner, Observer { result ->
            Log.e(HomeFragment.TAG, Gson().toJson(result))
            adapter.submitList(result?.data)
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e(TAG, "onMapReady")
        mMap = googleMap
        mMap.uiSettings.apply {
            isScrollGesturesEnabled = false
            isZoomGesturesEnabled = false
        }
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
