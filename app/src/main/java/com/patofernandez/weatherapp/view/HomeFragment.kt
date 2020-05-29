package com.patofernandez.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.location.LocationServices
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.model.CurrentWeatherApiResponse
import com.patofernandez.weatherapp.utils.Utils
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    @BindView(R.id.city) lateinit var city :TextView
    @BindView(R.id.country) lateinit var country :TextView
    @BindView(R.id.imgWeather) lateinit var imgWeather :ImageView
    @BindView(R.id.temperature) lateinit var temperature :TextView
    @BindView(R.id.weather) lateinit var weather :TextView
    @BindView(R.id.time) lateinit var time :TextView
    @BindView(R.id.visibility) lateinit var visibility :TextView
    @BindView(R.id.humidity) lateinit var humidity :TextView
    @BindView(R.id.pressure) lateinit var pressure :TextView
    @BindView(R.id.sunrise) lateinit var sunrise :TextView
    @BindView(R.id.sunset) lateinit var sunset :TextView
    @BindView(R.id.customForecast) lateinit var customForecast :CustomWeatherForecastView

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        imgWeather.setOnClickListener {
            updateView()
        }
        updateView()
    }

    private fun updateView() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            viewModel.getCurrentWeatherByCoords(location.latitude, location.longitude)
                .observe(requireActivity(), Observer { currentWeather ->
                    Log.e("sasa", "Location accuracy ${location.latitude} ${location.longitude}")
                    currentWeather?.let {
                        city.text = currentWeather.name
                        currentWeather.sys?.let { sys ->
                            country.text = ", ${sys.country}"
                            sunrise.text = Utils.formatedTime(sys.sunrise)
                            sunset.text = Utils.formatedTime(sys.sunset)
                        }
                        currentWeather.main?.let { main ->
                            pressure.text = main.pressure.toString().plus(" hpa")
                            temperature.text = Utils.formatedKelvinToCelsius(main.temp)
                            humidity.text = main.humidity.toString().plus(" %")
                        }
                        visibility.text = Utils.formatedVisibility(currentWeather.visibility)
                        time.text = Utils.formatedDate(currentWeather.dt)
                        currentWeather.weather.first()?.let {
                            weather.text = it.description
                            Picasso
                                .get()
                                .load("https://openweathermap.org/img/wn/${it.icon}@4x.png")
                                .into(imgWeather)
                        }
                    }
                })
            viewModel.getWeatherForecastByCoords(location.latitude, location.longitude)
                .observe(requireActivity(), Observer {
                    customForecast.setWeatherForecastData(
                        it,
                        object : CustomWeatherForecastView.OnForecastClickListener {
                            override fun onForecastClick(currentWeatherApiResponse: CurrentWeatherApiResponse) {
                                Toast.makeText(
                                    requireContext(),
                                    "Forecast clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    Log.e("asas", it.toString())
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

}
