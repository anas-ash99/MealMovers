package com.example.mealmoverskotlin.ui.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityOrdersHistoryBinding
import com.example.mealmoverskotlin.domain.viewModels.OrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrdersHistoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityOrdersHistoryBinding
    private val viewModel:OrderHistoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_orders_history)
        viewModel.init(this, binding)

    }



}