package com.patofernandez.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
        setContentView(R.layout.main_activity)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        checkPermission()
    }

    private fun checkPermission() {
        val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED
        Log.e(PermissionsActivity.TAG, "checkPermission")
        if (ContextCompat.checkSelfPermission(this, fineLocation) == granted ||
            ContextCompat.checkSelfPermission(this, coarseLocation) == granted) {
            Log.e(TAG, "PERMISO OK")
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.apply {
                addOnSuccessListener { location ->
                    Log.e(TAG, "Location ${Gson().toJson(location)}") // ${location.latitude} ${location.longitude}")
                }
                addOnFailureListener {
                    Log.e(TAG, "failure ${it.message.toString()}")
                }
                addOnCanceledListener {
                    Log.e(TAG, "canceled")
                }
                addOnCompleteListener {
                    Log.e(TAG, "complete ${Gson().toJson(it)}")
                }
            }
        } else {
            Log.e(TAG, "PERMISO COMO EL ORTO")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    fineLocation,
                    coarseLocation
                ),
                PermissionsActivity.PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    companion object {
        const val TAG = "MainActivity"
    }

}
