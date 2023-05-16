package com.example.mealmoverskotlin.ui.address

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityAddressBinding
import com.example.mealmoverskotlin.domain.LastSeenLocation
import com.example.mealmoverskotlin.domain.geoapify.Geoapify
import com.example.mealmoverskotlin.domain.viewModels.AddAddressViewModel
import com.example.mealmoverskotlin.shared.Constants
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.KeyboardManger
import com.example.mealmoverskotlin.shared.KeyboardManger.hideSoftKeyboard
import com.example.mealmoverskotlin.shared.KeyboardManger.showSoftKeyboard
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {

    private val viewModel:AddAddressViewModel by viewModels()
    private lateinit var binding:ActivityAddressBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_address)
        binding.addressSearchBar.animate().translationY(-163.0f).duration = 0
        viewModel.init(this, binding)



    }

    override fun onStart() {
        super.onStart()
        if (DataHolder.userAddress != null){
            binding.address = DataHolder.userAddress
            viewModel.initAddress(DataHolder.userAddress!!)
        }
    }

    override fun onBackPressed() {

        if (binding.addAddressLayout.isShown){
            viewModel.hideSearchBar()
        }else{
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(applicationContext, resultCode.toString(), Toast.LENGTH_SHORT).show()
             if (resultCode == Constants.ADDRESS_RESULT_CODE){
                 println(data)

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
                 viewModel.setCurrentLocation()
                // Permission is granted. Continue the action or workflow
                // in your app.
            } else {
                Toast.makeText(this, "Permission was rejected", Toast.LENGTH_SHORT).show()
                // Explain to the user that the feature is unavailable because
                // the feature requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }

        }
    }


}