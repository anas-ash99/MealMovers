package com.example.mealmoverskotlin.ui.mainPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityMainBinding
import com.example.mealmoverskotlin.domain.viewModels.MainPageViewModel
import com.example.mealmoverskotlin.shared.DataHolder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel:MainPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.initPage(this,  binding, getSharedPreferences("PROFILE", MODE_PRIVATE))


    }

    override fun onStart() {
        super.onStart()
        if (DataHolder.userAddress == null){
            binding.topNavbar.addressHeaderTV.text = "Select your address"
        }else{
            binding.topNavbar.addressHeaderTV.text = "${DataHolder.userAddress?.streetName}  ${DataHolder.userAddress?.houseNumber}"

        }
    }


}