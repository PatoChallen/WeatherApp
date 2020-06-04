package com.patofernandez.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.adapters.FavoriteLocationsAdapter
import com.patofernandez.weatherapp.di.Injectable
import com.patofernandez.weatherapp.vo.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel

class HomeFragment : Fragment(), Injectable {

    private lateinit var viewModel: WeatherViewModel

    @BindView(R.id.btnPreferences) lateinit var btnSelectLocation: FloatingActionButton
    @BindView(R.id.favoriteLocations) lateinit var mFavoriteLocations: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e(TAG, "onCreateView")
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "onActivityCreated")
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        btnSelectLocation.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_selectLocationFragment)
        }
        viewModel.getFavoriteLocations().observe(requireActivity(), Observer {
            mFavoriteLocations.adapter = FavoriteLocationsAdapter(it, object : FavoriteLocationsAdapter.OnFavoriteActionListener {
                override fun onFavoriteLocationClick(favoriteLocation: CurrentWeatherApiResponse) {
                    viewModel.setCurrentWeather(favoriteLocation)
                    findNavController().navigate(R.id.action_homeFragment_to_locationWeatherFragment)
                }

                override fun onFavoriteLocationDelete(favoriteLocation: CurrentWeatherApiResponse) {
                    viewModel.removeFavoriteLocation(favoriteLocation)
                }
            })
        })
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}
