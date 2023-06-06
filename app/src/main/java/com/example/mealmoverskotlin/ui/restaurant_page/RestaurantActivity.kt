package com.example.mealmoverskotlin.ui.restaurant_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityRestaurantBinding
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCartVM
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RestaurantActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRestaurantBinding
    val viewModel:RestaurantAndCartVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant)
        viewModel.init(this)
        startFragment()



    }



    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag("search_fragment")?.let {
            viewModel.itemsSearchFor.value = listOf()
        }

        super.onBackPressed()
    }


    private fun startFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, RestaurantFragment(), "restaurant_fragment")
            commit()
        }
    }

}