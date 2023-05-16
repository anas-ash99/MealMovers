package com.example.mealmoverskotlin.domain

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.mealmoverskotlin.BuildConfig
import com.example.mealmoverskotlin.shared.DataHolder
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import java.util.concurrent.TimeUnit


object LastSeenLocation {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null


    @SuppressLint("MissingPermission")
    fun setLastSeenLocation(activity:Activity){
        val googleApiClient = GoogleApiClient.Builder(activity)
            .addApi(LocationServices.API).build();
            googleApiClient.connect()


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            isWaitForAccurateLocation  = true
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(activity,
                        1)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                DataHolder.myLatLng.value = LatLng(locationResult.lastLocation?.latitude!!, locationResult.lastLocation?.longitude!!)
            }
        }



        task.addOnSuccessListener { locationSettingsResponse ->

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }



    }



    fun askForLocationPermission(activity: Activity) {
        val requestCode:Int = 1
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                   ),
                   requestCode
            )
           println("not granted")
        }else{
            println("granted")
            setLastSeenLocation(activity)
        }
    }
}
