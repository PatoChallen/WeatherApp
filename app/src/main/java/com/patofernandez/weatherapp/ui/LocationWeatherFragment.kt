package com.patofernandez.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.patofernandez.weatherapp.AppExecutors
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.adapters.WeatherHoursAdapter
import com.patofernandez.weatherapp.binding.FragmentDataBindingComponent
import com.patofernandez.weatherapp.databinding.HomeFragmentBinding
import com.patofernandez.weatherapp.databinding.SelectLocationFragmentBinding
import com.patofernandez.weatherapp.di.Injectable
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.ui.common.RetryCallback
import com.patofernandez.weatherapp.utils.FormatUtils
import com.patofernandez.weatherapp.utils.autoCleared
import com.patofernandez.weatherapp.view.CustomWeatherForecastView
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

class LocationWeatherFragment : Fragment(), OnMapReadyCallback, Injectable {

    private lateinit var mMap: GoogleMap

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<SelectLocationFragmentBinding>()

    val weatherViewModel: com.patofernandez.weatherapp.ui.WeatherViewModel by viewModels {
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
        val rvAdapter = FavoriteLocationsAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) { repo ->
//            findNavController().navigate(
//                SearchFragmentDirections.showRepo(repo.owner.login, repo.name)
//            )
        }
//        binding.query = weatherViewModel.query
//        binding.favoriteLocations.adapter = rvAdapter
//        adapter = rvAdapter

//        binding.callback = object : RetryCallback {
//            override fun retry() {
////                weatherViewModel.refresh()
//            }
//        }
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        Log.e(TAG, "onCreateView")
//        val view = inflater.inflate(R.layout.location_weather_fragment, container, false)
//        ButterKnife.bind(this, view)
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//        return view
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        Log.e(TAG, "onActivityCreated")
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
//        mImgWeather.setOnClickListener {
//            updateView()
//        }
//    }
//
//    private fun updateView() {
//        Log.e(TAG, "updateView")
//        viewModel.getWeatherForecast().observe(requireActivity(), Observer {
//            Log.e(TAG, Gson().toJson(it))
//            if (it == null) return@Observer
//                mCustomForecast.setWeatherForecastData(
//                    it,
//                    object : CustomWeatherForecastView.OnForecastClickListener {
//                        override fun onForecastClick(currentWeather: CurrentWeatherApiResponse) {
//                            updateActualView(currentWeather)
//                        }
//                    })
//                it.city?.let { city ->
//                    city.coord?.let {  coord ->
//                        updateMap(LatLng(coord.latitude, coord.longitude), city.name)
//                    }
//                    mCity.text = "${city.name}, ${city.country}"
//                    mSunrise.text = FormatUtils.formatedTime(city.sunrise)
//                    mSunset.text = FormatUtils.formatedTime(city.sunset)
//                }
//                updateActualView(it.list.first())
//            })
//    }
//
//    private fun updateActualView(currentWeather: CurrentWeatherApiResponse?) {
//        Log.e(TAG, "updateActualView")
//        currentWeather?.let {
//            val hourDara = viewModel.getWheaterHoursByDay(currentWeather.date)
//            val currentWeather = hourDara.first()
//            currentWeather.main?.let { main ->
//                mPressure.text = getString(R.string.pressure_text, main.pressure)
//                mTemperature.text = FormatUtils.formatedKelvinToCelsius(main.temp)
//                mHumidity.text = getString(R.string.humidity_text, main.humidity)
//            }
//            mDate.text = FormatUtils.formatedDate(currentWeather.date)
//            currentWeather.weather.first()?.let {
//                Picasso
//                    .get()
//                    .load("https://openweathermap.org/img/wn/${it.icon}@4x.png")
//                    .into(mImgWeather)
//            }
//            weathweHours.adapter = WeatherHoursAdapter(hourDara)
//        }
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e(TAG, "onMapReady")
        mMap = googleMap
        mMap.uiSettings.apply {
            isScrollGesturesEnabled = false
            isZoomGesturesEnabled = false
        }
//        updateView()
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
