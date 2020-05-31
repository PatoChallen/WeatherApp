package com.patofernandez.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.patofernandez.weatherapp.utils.Preferences
import com.patofernandez.weatherapp.viewmodel.GeocodingViewModel
import kotlinx.android.synthetic.main.select_location_fragment.*

class SelectLocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: GeocodingViewModel
    private var mMap: GoogleMap? = null

    @BindView(R.id.city) lateinit var mCity: TextView
    @BindView(R.id.country) lateinit var mCountry: TextView
    @BindView(R.id.region) lateinit var mRegion: TextView
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
        Log.e(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        btnConfirm.setOnClickListener {
            lastMarker?.let {
                viewModel.addFavoriteLocation(it.position)
            }
            findNavController().navigate(R.id.action_selectLocationFragment_to_homeFragment)
        }
        mCity.setOnClickListener {
            Log.e(TAG, "Favorite Locations: ${Gson().toJson(viewModel.getFavoriteLocations())}")
        }
        mCountry.setOnClickListener {
            Preferences.favoriteLocations.clear()
        }
        viewModel = ViewModelProvider(requireActivity()).get(GeocodingViewModel::class.java)
        viewModel.getReverseGeocoding().observe(requireActivity(), Observer { reverseGeocoding ->
            if (reverseGeocoding == null) {
                showLoader()
                attemps ++
                if (attemps <= MAX_ATTEMPS) {
                    lastMarker?.let {
                        with(it.position) {
                            viewModel.setCoordinates(latitude, longitude)
                        }
                    }
                } else {
                    attemps = 0
                }
            } else {
                hideLoader()
                attemps = 0
                with (reverseGeocoding) {
                    mCity.text = city
                    mCountry.text = country
                    mRegion.text = region
                    mLatLng.text = "$latitude, $longitude"
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
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.e(TAG, "Location accuracy ${location.latitude} ${location.longitude}")
            setCurrentLocationToMap(LatLng(location.latitude, location.longitude))
        }
    }

    private fun updateMap(latLng: LatLng, title: String) {
        Log.e(TAG, "updateMap LatLng: ${Gson().toJson(latLng)}")
        mMap!!.apply {
            lastMarker?.remove()
            lastMarker = addMarker(MarkerOptions().position(latLng).title(title))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_DEFAULT))
        }
        viewModel.setCoordinates(latLng.latitude, latLng.longitude)
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
