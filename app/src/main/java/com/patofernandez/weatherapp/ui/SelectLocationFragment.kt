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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.binding.FragmentDataBindingComponent
import com.patofernandez.weatherapp.databinding.SelectLocationFragmentBinding
import com.patofernandez.weatherapp.di.Injectable
import com.patofernandez.weatherapp.utils.autoCleared
import com.patofernandez.weatherapp.vo.Status
import javax.inject.Inject

class SelectLocationFragment : Fragment(), OnMapReadyCallback, Injectable {

    private lateinit var mMap: GoogleMap
    private var lastMarker: Marker? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<SelectLocationFragmentBinding>()

    private val weatherViewModel: WeatherViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.select_location_fragment,
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
        binding.btnConfirm.setOnClickListener {
            weatherViewModel.addSelectedLocationToFavorites()
            requireActivity().onBackPressed()
        }
        weatherViewModel.selectedLocation.observe(viewLifecycleOwner, Observer { result ->
            Log.e(TAG, Gson().toJson(result))
            if (result.status == Status.SUCCESS){
                binding.currentWeather = result.data
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e(TAG, "onMapReady")
        mMap = googleMap
        mMap.apply {
            uiSettings.isZoomControlsEnabled = true
            setOnMapClickListener { latLng->
                updateMap(latLng, "Nueva Selección")
            }
        }
    }

    private fun updateMap(latLng: LatLng, title: String) {
        Log.e(TAG, "updateMap LatLng: ${Gson().toJson(latLng)}")
        mMap.apply {
            lastMarker?.remove()
            lastMarker = addMarker(MarkerOptions().position(latLng).title(title))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT))
        }
        weatherViewModel.setSelectedLatLng(latLng)
//        showLoader()
    }

    companion object {
        const val TAG = "SelectLocationFragment"
        const val ZOOM_DEFAULT = 10F
    }

}
