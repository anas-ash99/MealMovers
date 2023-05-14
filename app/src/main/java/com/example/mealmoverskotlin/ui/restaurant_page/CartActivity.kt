package com.example.mealmoverskotlin.ui.restaurant_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityCartBinding
import com.example.mealmoverskotlin.domain.viewModels.CartPageViewModel

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private val viewModel:CartPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_cart)
        viewModel.init(this, binding)

        onArrowBackClick()
    }
    private fun onArrowBackClick() {
        binding.backArrow.setOnClickListener {
          onBackPressed()
        }
    }
}