package com.example.mealmoverskotlin.ui.mainPage

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityMainBinding
import com.example.mealmoverskotlin.domain.network_connection.NetworkConnection
import com.example.mealmoverskotlin.domain.viewModels.MainPageViewModel
import com.example.mealmoverskotlin.domain.stripe.StripeUseCase
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.PriceTrimmer
import com.stripe.model.Price
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel:MainPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.initPage(this,this,   binding, getSharedPreferences("PROFILE", MODE_PRIVATE))
        val network =NetworkConnection(this)
        network.observe(this){
            if (!it){
                binding.networkError.layout.visibility =View.VISIBLE
            }else{
                binding.networkError.layout.visibility =View.GONE
            }
        }


    }



    private fun checkInternetConnection():Boolean{
        val connectivityManager:ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo:NetworkInfo = connectivityManager.activeNetworkInfo()
        return false

    }
    override fun onStart() {
        super.onStart()
        if (DataHolder.userAddress == null){
            binding.topNavbar.addressHeader.text = "Select your address"
        }else{
            binding.topNavbar.addressHeader.text = "${DataHolder.userAddress?.streetName}  ${DataHolder.userAddress?.houseNumber}"

        }
    }


}