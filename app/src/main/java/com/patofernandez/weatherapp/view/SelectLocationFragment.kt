package com.patofernandez.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.patofernandez.weatherapp.R
import com.patofernandez.weatherapp.utils.FormatUtils
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.select_location_fragment.*

class SelectLocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var weatherViewModel: WeatherViewModel
    private var mMap: GoogleMap? = null

    @BindView(R.id.imgWeather) lateinit var mImgWeather: ImageView
    @BindView(R.id.city) lateinit var mCity: TextView
    @BindView(R.id.temp) lateinit var mTemp: TextView
    @BindView(R.id.tempMax) lateinit var mTempMax: TextView
    @BindView(R.id.tempMin) lateinit var mTempMin: TextView
    @BindView(R.id.latLng) lateinit var mLatLng: TextView
    @BindView(R.id.progress) lateinit var mProgress: ProgressBar
    @BindView(R.id.cardDetails) lateinit var mCardDetails: CardView
    @BindView(R.id.instructions) lateinit var mInstructions: TextView
    @BindView(R.id.btnConfirm) lateinit var mBtnConfirm: Button

    private var lastMarker: Marker? = null
    private var attemps = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e(TAG, "onCreateView")
        val view = inflater.inflate(R.layout.select_location_fragment, container, false)
        ButterKnife.bind(this, view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "onActivityCreated")
        mInstructions.visibility = View.VISIBLE
        weatherViewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        btnConfirm.setOnClickListener {
            weatherViewModel.addSelectedLocationToFavorites()
            requireActivity().onBackPressed()
        }
        weatherViewModel.getSelectedLocation().observe(requireActivity(), Observer { selectedLocation ->
            if (selectedLocation == null) {
                showLoader()
                attemps ++
                if (attemps <= MAX_ATTEMPS) {
                    lastMarker?.let {
                        with(it.position) {
                            weatherViewModel.setCoordinates(latitude, longitude)
                        }
                    }
                } else {
                    attemps = 0
                }
            } else {
                hideLoader()
                attemps = 0
                with (selectedLocation) {
                    mCity.text = "$name, ${sys?.country}"
                    weather.first()?.let { weather ->
                        Picasso
                            .get()
                            .load("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
                            .into(mImgWeather)
                    }
                    main?.let { main ->
                        mTemp.text = FormatUtils.formatedKelvinToCelsius(main.temp)
                        mTempMax.text = FormatUtils.formatedKelvinToCelsius(main.tempMax)
                        mTempMin.text = FormatUtils.formatedKelvinToCelsius(main.tempMin)
                    }
                    coordinates?.let { coordinates ->
                        mLatLng.text = "${coordinates.latitude}, ${coordinates.longitude}"
                    }
                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e(TAG, "onMapReady")
        mMap = googleMap
        mMap!!.apply {
            uiSettings.isZoomControlsEnabled = true
            setOnMapClickListener { latLng->
                updateMap(latLng, "Nueva Selección")
            }
        }
    }

    private fun updateMap(latLng: LatLng, title: String) {
        Log.e(TAG, "updateMap LatLng: ${Gson().toJson(latLng)}")
        mMap!!.apply {
            lastMarker?.remove()
            lastMarker = addMarker(MarkerOptions().position(latLng).title(title))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT))
        }
        weatherViewModel.setCoordinates(latLng.latitude, latLng.longitude)
        showLoader()
    }

    private fun showLoader() {
        mProgress.visibility = View.VISIBLE
        mCardDetails.visibility = View.GONE
        mInstructions.visibility = View.GONE
    }

    private fun hideLoader() {
        mProgress.visibility = View.GONE
        mCardDetails.visibility = View.VISIBLE
        mInstructions.visibility = View.GONE
    }

    private fun setCurrentLocationToMap(latLng: LatLng) {
        Log.e(TAG, "setCurrentLocationToMap")
        mMap!!.apply {
            addMarker(MarkerOptions().position(latLng).title("Su ubicación"))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT))
        }
    }

    companion object {
        const val TAG = "SelectLocationFragment"
        const val ZOOM_DEFAULT = 10F
        const val MAX_ATTEMPS = 3
    }

}
