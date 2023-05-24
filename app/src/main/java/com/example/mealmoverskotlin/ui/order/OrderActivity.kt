package com.example.mealmoverskotlin.ui.order

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.mealmoverskotlin.ui.mainPage.MainActivity
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
import io.grpc.android.BuildConfig
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrderActivity : AppCompatActivity() {

    private lateinit var binding:ActivityOrderBinding
    private val viewModel:OrderPageViewModel by viewModels()
    private var isAfterOrdered:Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        viewModel.init(this, binding)
        isAfterOrdered = intent.getBooleanExtra("isAfterOrdered", false)
    }


    override fun onBackPressed() {

        if (isAfterOrdered == false || isAfterOrdered == null){
            super.onBackPressed()
        }else if(isAfterOrdered == true) {
            val intent  = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }


}