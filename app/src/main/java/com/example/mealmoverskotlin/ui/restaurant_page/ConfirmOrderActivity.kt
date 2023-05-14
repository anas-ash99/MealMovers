package com.example.mealmoverskotlin.ui.restaurant_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.viewModels.ConfirmOrderPageViewModel
import com.example.mealmoverskotlin.shared.KlarnaConst
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentCategory
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentView
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentViewCallback
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentsSDKError
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class ConfirmOrderActivity : AppCompatActivity() {
    private lateinit var binding:com.example.mealmoverskotlin.databinding.ActivityConfirmOrderBinding
    private val viewModel:ConfirmOrderPageViewModel by viewModels()
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