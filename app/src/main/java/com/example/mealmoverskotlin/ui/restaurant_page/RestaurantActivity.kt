package com.example.mealmoverskotlin.ui.restaurant_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.*
import com.example.mealmoverskotlin.databinding.ActivityRestaurantBinding
import com.example.mealmoverskotlin.domain.viewModels.RestaurantActivityViewModel
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCheckoutVM
import com.example.mealmoverskotlin.shared.DataHolder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RestaurantActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRestaurantBinding
    val viewModel:RestaurantAndCheckoutVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant)
        viewModel.init(this)
        startFragment()



    }



    private fun startFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, RestaurantFragment(), "restaurant_fragment")
            commit()
        }
    }

}