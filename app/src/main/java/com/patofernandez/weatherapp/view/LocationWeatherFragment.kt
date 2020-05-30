package com.patofernandez.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.adapters.WeatherHoursAdapter
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.utils.FormatUtils
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso

class LocationWeatherFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    @BindView(R.id.city) lateinit var mCity :TextView
    @BindView(R.id.imgWeather) lateinit var mImgWeather :ImageView
    @BindView(R.id.temperature) lateinit var mTemperature :TextView
    @BindView(R.id.date) lateinit var mDate :TextView
    @BindView(R.id.humidity) lateinit var mHumidity :TextView
    @BindView(R.id.pressure) lateinit var mPressure :TextView
    @BindView(R.id.sunrise) lateinit var mSunrise :TextView
    @BindView(R.id.sunset) lateinit var mSunset :TextView
    @BindView(R.id.weathweHours) lateinit var weathweHours :RecyclerView
    @BindView(R.id.customForecast) lateinit var mCustomForecast :CustomWeatherForecastView

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.location_weather_fragment, container, false)
        ButterKnife.bind(this, view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        mImgWeather.setOnClickListener {
            updateView()
        }
        updateView()
    }

    private fun updateView() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.e(TAG, "Location accuracy ${location.latitude} ${location.longitude}")
            viewModel.getWeatherForecastByCoords(location.latitude, location.longitude)
                .observe(requireActivity(), Observer {
                    mCustomForecast.setWeatherForecastData(
                        it,
                        object : CustomWeatherForecastView.OnForecastClickListener {
                            override fun onForecastClick(currentWeather: CurrentWeatherApiResponse) {
                                updateActualView(currentWeather)
                            }
                        })
                    it.city?.let { city ->
                        updateMap(LatLng(location.latitude, location.longitude), city.name)
                        mCity.text = "${city.name}, ${city.country}"
                        mSunrise.text = FormatUtils.formatedTime(city.sunrise)
                        mSunset.text = FormatUtils.formatedTime(city.sunset)
                    }
                    updateActualView(it.list.first())
                })
        }
        fusedLocationClient.lastLocation.addOnCompleteListener {
            Log.e("sasa", "Location confirmed")
        }
        fusedLocationClient.lastLocation.addOnFailureListener {
            Log.e("sasa", "Location Failure ${it.message}")
        }
        fusedLocationClient.lastLocation.addOnCanceledListener {
            Log.e("sasa", "Location Canceled")
        }
    }

    private fun updateActualView(currentWeather: CurrentWeatherApiResponse?) {
        currentWeather?.let {
            val hourDara = viewModel.getWheaterHoursByDay(currentWeather.date)
            val currentWeather = hourDara.first()
            currentWeather.main?.let { main ->
                mPressure.text = getString(R.string.pressure_text, main.pressure)
                mTemperature.text = FormatUtils.formatedKelvinToCelsius(main.temp)
                mHumidity.text = getString(R.string.humidity_text, main.humidity)
            }
            mDate.text = FormatUtils.formatedDate(currentWeather.date)
            currentWeather.weather.first()?.let {
                Picasso
                    .get()
                    .load("https://openweathermap.org/img/wn/${it.icon}@4x.png")
                    .into(mImgWeather)
            }
            weathweHours.adapter = WeatherHoursAdapter(hourDara)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply {
            isScrollGesturesEnabled = false
            isZoomGesturesEnabled = false
        }
    }

    private fun updateMap(latLng: LatLng, title: String) {
        mMap.apply {
            addMarker(MarkerOptions().position(latLng).title(title))
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT))
        }
    }

    companion object {
        const val TAG = "HomeFragment"
        const val ZOOM_DEFAULT = 12F
    }

}
