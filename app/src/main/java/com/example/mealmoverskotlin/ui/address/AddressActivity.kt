package com.example.mealmoverskotlin.ui.address

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityAddressBinding
import com.example.mealmoverskotlin.shared.LastSeenLocation
import com.example.mealmoverskotlin.domain.viewModels.AddAddressViewModel
import com.example.mealmoverskotlin.shared.Constants
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.ui.mainPage.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {

    private val viewModel:AddAddressViewModel by viewModels()
    private lateinit var binding:ActivityAddressBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_address)
//        binding.addressSearchBar.animate().translationY(-163.0f).duration = 0
//        viewModel.init(this, binding)
        viewModel.isAfterSignUp = intent.getBooleanExtra("isAfterSignUp", false)
        startFragment()



    }

    private fun startFragment() {
        supportFragmentManager.commit {
            replace(R.id.fragment_layout, AddressHomePageFragment())

        }
    }

    override fun onStart() {
        super.onStart()
        DataHolder.userAddress?.let{
//            binding.address = it
//            viewModel.initAddress(it)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        if (viewModel.isAddAddressLayoutShown.value == true){
            viewModel.isAddAddressLayoutShown.value = false
        }
        else if (viewModel.isAfterSignUp == true){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            super.onBackPressed()
        }

//        if (binding.addAddressLayout.isShown){
//            viewModel.hideSearchBar()
//        }else{
//            super.onBackPressed()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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

                 viewModel.setSuggestedAddress()
                // Permission is granted. Continue the action or workflow
                // in your app.
            } else {
                Toast.makeText(this, "Permission was rejected", Toast.LENGTH_SHORT).show()
                viewModel.isLocationPermissionGranted.value  = false
                // Explain to the user that the feature is unavailable because
                // the feature requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }

        }
    }


}