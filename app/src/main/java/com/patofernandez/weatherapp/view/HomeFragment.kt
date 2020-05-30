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
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.utils.Utils
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    @BindView(R.id.city) lateinit var mCity :TextView
    @BindView(R.id.imgWeather) lateinit var mImgWeather :ImageView
    @BindView(R.id.temperature) lateinit var mTemperature :TextView
    @BindView(R.id.date) lateinit var mDate :TextView
//    @BindView(R.id.visibility) lateinit var mVisibility :TextView
    @BindView(R.id.humidity) lateinit var mHumidity :TextView
    @BindView(R.id.pressure) lateinit var mPressure :TextView
    @BindView(R.id.sunrise) lateinit var mSunrise :TextView
    @BindView(R.id.sunset) lateinit var mSunset :TextView
    @BindView(R.id.customForecast) lateinit var mCustomForecast :CustomWeatherForecastView

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
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
//            viewModel.getCurrentWeatherByCoords(location.latitude, location.longitude)
//                .observe(requireActivity(), Observer { currentWeather ->
//                    Log.e("sasa", "Location accuracy ${location.latitude} ${location.longitude}")
//                    currentWeather?.let {
//                        updateMap(LatLng(location.latitude, location.longitude), currentWeather.name)
//                        mCity.text = currentWeather.name
//                        currentWeather.sys?.let { sys ->
//                            mCountry.text = ", ${sys.country}"
//                            mSunrise.text = Utils.formatedTime(sys.sunrise)
//                            mSunset.text = Utils.formatedTime(sys.sunset)
//                        }
//                        currentWeather.main?.let { main ->
//                            pressure.text = main.pressure.toString().plus(" hpa")
//                            temperature.text = Utils.formatedKelvinToCelsius(main.temp)
//                            humidity.text = main.humidity.toString().plus(" %")
//                        }
//                        visibility.text = Utils.formatedVisibility(currentWeather.visibility)
//                        date.text = Utils.formatedDate(currentWeather.dt)
//                        currentWeather.weather.first()?.let {
//                            Picasso
//                                .get()
//                                .load("https://openweathermap.org/img/wn/${it.icon}@4x.png")
//                                .into(imgWeather)
//                        }
//                    }
//                })
            Log.e("sasa", "Location accuracy ${location.latitude} ${location.longitude}")
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
                        mSunrise.text = Utils.formatedTime(city.sunrise)
                        mSunset.text = Utils.formatedTime(city.sunset)
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
            currentWeather.main?.let { main ->
                mPressure.text = "Presión: ${main.pressure} hpa"
                mTemperature.text = Utils.formatedKelvinToCelsius(main.temp)
                mHumidity.text = "Humedad: ${main.humidity} %"
            }
//            mVisibility.text = Utils.formatedVisibility(currentWeather.visibility)
            mDate.text = Utils.formatedDate(currentWeather.dt)
            currentWeather.weather.first()?.let {
                Picasso
                    .get()
                    .load("https://openweathermap.org/img/wn/${it.icon}@4x.png")
                    .into(mImgWeather)
            }
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
        const val ZOOM_DEFAULT = 12F
    }

}