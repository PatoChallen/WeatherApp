package com.patofernandez.weatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.adapters.FavoriteLocationsAdapter
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel

    @BindView(R.id.btnSelectLocation) lateinit var btnSelectLocation: Button
    @BindView(R.id.btnLocationWeather) lateinit var btnLocationWeather: Button
    @BindView(R.id.favoriteLocations) lateinit var mFavoriteLocations: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        btnSelectLocation.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_selectLocationFragment)
        }
        btnLocationWeather.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_locationWeatherFragment)
        }
        mFavoriteLocations.adapter = FavoriteLocationsAdapter(viewModel.getFavoriteLocations())
    }

}
