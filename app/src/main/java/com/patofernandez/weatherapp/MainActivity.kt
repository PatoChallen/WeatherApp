package com.patofernandez.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.patofernandez.weatherapp.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
        setContentView(R.layout.main_activity)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
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
    }

    companion object {
        const val TAG = "MainActivity"
    }

}
