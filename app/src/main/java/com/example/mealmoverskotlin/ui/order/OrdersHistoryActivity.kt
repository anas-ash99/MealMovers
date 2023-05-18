package com.example.mealmoverskotlin.ui.order

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityOrdersHistoryBinding
import com.example.mealmoverskotlin.domain.LastSeenLocation
import com.example.mealmoverskotlin.domain.adapters.OrderHistoryAdapter
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