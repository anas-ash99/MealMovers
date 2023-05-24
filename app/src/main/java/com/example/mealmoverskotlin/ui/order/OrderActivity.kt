package com.example.mealmoverskotlin.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityOrderBinding
import com.example.mealmoverskotlin.domain.viewModels.OrderPageViewModel
import com.example.mealmoverskotlin.ui.mainPage.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderActivity : AppCompatActivity() {

    private lateinit var binding:ActivityOrderBinding
    private val viewModel:OrderPageViewModel by viewModels()
    private var isAfterOrdered:Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        viewModel.init(this, binding)
        isAfterOrdered = intent.getBooleanExtra("isAfterOrdered", false)
    }


    override fun onBackPressed() {

        if (isAfterOrdered == false || isAfterOrdered == null){
            super.onBackPressed()
        }else if(isAfterOrdered == true) {
            val intent  = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }


}