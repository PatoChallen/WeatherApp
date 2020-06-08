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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.binding.FragmentDataBindingComponent
import com.patofernandez.weatherapp.databinding.HomeFragmentBinding
import com.patofernandez.weatherapp.di.Injectable
import com.patofernandez.weatherapp.utils.autoCleared
import com.patofernandez.weatherapp.vo.FavoriteLocation
import com.patofernandez.weatherapp.vo.Status
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<HomeFragmentBinding>()

    private var adapter by autoCleared<FavoriteLocationsAdapter>()

    private val weatherViewModel: WeatherViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment,
            container,
            false,
            dataBindingComponent
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        val favoriteLocationsAdapter = FavoriteLocationsAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors,
            onFavoriteActionListener = object : FavoriteLocationsAdapter.OnFavoriteActionListener {
                override fun onFavoriteLocationClick(favoriteLocation: FavoriteLocation) {
                    weatherViewModel.setForecastLatLng(LatLng(favoriteLocation.lat, favoriteLocation.lng))
                    findNavController().navigate(R.id.action_homeFragment_to_locationWeatherFragment)
                }
                override fun onFavoriteLocationDelete(favoriteLocation: FavoriteLocation) {
                    weatherViewModel.removeLocationFromFavorites(favoriteLocation)
                }
            }
        )

        binding.favoriteLocations.adapter = favoriteLocationsAdapter
        adapter = favoriteLocationsAdapter
        binding.btnAddLocation.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_selectLocationFragment)
        }
        binding.myLocation.setOnClickListener {
            weatherViewModel.setForecastLatLng()
            findNavController().navigate(R.id.action_homeFragment_to_locationWeatherFragment)
        }
        weatherViewModel.currentLocation.observe(viewLifecycleOwner, Observer { result ->
            Log.e(TAG, Gson().toJson(result))
            if (result.status == Status.SUCCESS){
                binding.currentLocation = result.data
            }
        })
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.apply {
            addOnSuccessListener { location ->
                Log.e(TAG, "OnSucces: ${Gson().toJson(location.latitude)}")
                weatherViewModel.setMyLatLng(LatLng(location.latitude, location.longitude))
            }
            addOnFailureListener {
                Log.e(TAG, "OnFailure: ${it.message}")
            }
        }
    }

    private fun initRecyclerView() {
        binding.result = weatherViewModel.results
        weatherViewModel.results.observe(viewLifecycleOwner, Observer { result ->
            Log.e(TAG, Gson().toJson(result))
            adapter.submitList(result?.data)
        })

    }

    companion object {
        const val TAG = "HomeFragment"
    }

}
