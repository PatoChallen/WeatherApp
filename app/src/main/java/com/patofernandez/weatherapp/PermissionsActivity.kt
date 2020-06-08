package com.patofernandez.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import kotlinx.android.synthetic.main.permissions_activity.*

class PermissionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
        setContentView(R.layout.permissions_activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            checkPermission()
        }
        btnAddLocation.setOnClickListener {
            val intent = Intent().apply {
                action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", packageName, null)
            }
            startActivityForResult(intent, PREFERENCE_REQUEST_CODE)
            btnAddLocation.visibility = View.VISIBLE
        }
        btnReintentar.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        val fineLocation = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED
        Log.e(TAG, "checkPermission")
        if (ContextCompat.checkSelfPermission(this, fineLocation) == granted ||
            ContextCompat.checkSelfPermission(this, coarseLocation) == granted) {
            goToMainActivity()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    fineLocation,
                    coarseLocation
                ),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e(TAG, "onRequestPermissionsResult ${Gson().toJson(grantResults)}")

        when(requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                when(grantResults.contains(RESULT_OK)) {
                    true ->
                        goToMainActivity()
                    false ->
                        if (shouldShowRequestPermissionRationale( permissions.first() )) {
                            btnAddLocation.visibility = View.GONE
                            preferences.visibility = View.GONE
                            Log.e(TAG, "Just Deny")
                        } else {
                            btnAddLocation.visibility = View.VISIBLE
                            preferences.visibility = View.VISIBLE
                            Log.e(TAG, "Don't ask again selected")
                        }
                }
            PREFERENCE_REQUEST_CODE -> checkPermission()
        }
    }

    private fun goToMainActivity() {
        apply {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }
        finish()
    }

    companion object {
        const val TAG = "PermissionsActivity"
        const val PERMISSIONS_REQUEST_CODE = 123
        const val PREFERENCE_REQUEST_CODE = 321
        const val RESULT_OK = 0
    }

}
