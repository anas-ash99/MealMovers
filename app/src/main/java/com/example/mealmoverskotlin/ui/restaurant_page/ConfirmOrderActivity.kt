package com.example.mealmoverskotlin.ui.restaurant_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.viewModels.OrderCheckoutPageViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class ConfirmOrderActivity : AppCompatActivity() {
    private lateinit var binding:com.example.mealmoverskotlin.databinding.ActivityConfirmOrderBinding
    private val viewModel:OrderCheckoutPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_confirm_order)
        viewModel.init(binding, this)
        viewModel.initDialog()
        onArrowBackClick()




    }

    private fun onArrowBackClick() {
        binding.backArrow.setOnClickListener {

            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (binding.klarnaLayout.visibility == View.VISIBLE){
            binding.progressBar.visibility = View.GONE
            binding.klarnaLayout.visibility = View.GONE
            binding.mainLayout.visibility = View.VISIBLE
        }else{
            super.onBackPressed()

        }
    }




}