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
import com.example.mealmoverskotlin.shared.DataHolder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RestaurantActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRestaurantBinding
    val viewModel:RestaurantActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= DataBindingUtil.setContentView(this, R.layout.activity_restaurant)

        viewModel.init(
            DataHolder.restaurant,
            this,
            this,
            binding
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        viewModel.
//        DataHolder.order.items.removeAll(mutableListOf())
//        DataHolder.order = OrderModel()

    }





}