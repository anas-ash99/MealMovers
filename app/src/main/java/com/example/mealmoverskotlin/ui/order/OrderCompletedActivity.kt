package com.example.mealmoverskotlin.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityOrderCompletedBinding
import com.example.mealmoverskotlin.ui.mainPage.MainActivity

class OrderCompletedActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderCompletedBinding
    private var orderId:String? = null
    private var restaurantId:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_order_completed)
        orderId = intent.getStringExtra("order_id")
        restaurantId = intent.getStringExtra("restaurantId")
        println(orderId + " order ")
        println(restaurantId)
        onArrowBack()
        onTrackButtonClick()
    }

    override fun onBackPressed() {
        val intent  =Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

    private fun onTrackButtonClick(){
        binding.trackOrderButton.setOnClickListener {

            val i = Intent(applicationContext, OrderActivity::class.java)
            i.putExtra( "order_id",orderId)
            i.putExtra( "restaurantId",restaurantId)
            i.putExtra("isAfterOrdered",true)
            startActivity(i)
            finish()
        }


    }
    private fun onArrowBack(){
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
    }

}