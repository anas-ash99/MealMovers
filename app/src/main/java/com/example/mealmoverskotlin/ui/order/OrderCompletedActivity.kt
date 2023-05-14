package com.example.mealmoverskotlin.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityOrderCompletedBinding
import com.example.mealmoverskotlin.ui.mainPage.MainActivity

class OrderCompletedActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderCompletedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_order_completed)
    }

    override fun onBackPressed() {
        val intent  =Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
//        finish()

    }
}