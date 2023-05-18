package com.example.mealmoverskotlin.ui.order

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityOrderBinding
import com.example.mealmoverskotlin.domain.LastSeenLocation
import com.example.mealmoverskotlin.domain.viewModels.OrderPageViewModel
import com.example.mealmoverskotlin.shared.DataHolder
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.stripe.exception.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderBinding
    private var locationPermissionGranted =false
    private val viewModel:OrderPageViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)

        checkLocationPermission()

    }

    private fun checkLocationPermission() {
        if (LastSeenLocation.isLocationPermissionGranted(this)){
            initMap()
            viewModel.init(this, binding)
        }else{
           LastSeenLocation.askForLocationPermission(this)

        }
    }


    private fun initMap() {

        try {
            val mapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment.getMapAsync {
                viewModel.map.value = it
            }
        }catch (e:Exception){
            Log.e("Map", e.message!!, e)
            Toast.makeText(this, "Something went wrong initializing the map", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
               1)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                LastSeenLocation.setLastSeenLocation(this)
                initMap()
                viewModel.init(this, binding)

            } else {
                Toast.makeText(this, "Permission was rejected", Toast.LENGTH_SHORT).show()
                binding.mapLayout.visibility = View.GONE
                viewModel.init(this, binding)

            }

        }
    }
}