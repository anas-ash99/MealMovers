package com.example.mealmoverskotlin.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.databinding.ActivityOrdersHistoryBinding
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
        binding.progressBar.visibility = View.VISIBLE
        binding.swipeRefreshLayout.visibility = View.GONE
        getOrders()
        onSwipeRefresh()
    }

    private fun getOrders() {
       viewModel.getOrders(){ orders, error ->
           orders?.let {
               initRecyclerView()
               binding.progressBar.visibility = View.GONE
               binding.swipeRefreshLayout.visibility = View.VISIBLE
               binding.swipeRefreshLayout.isRefreshing = false
           }

           error?.let {
               binding.progressBar.visibility = View.GONE
               binding.swipeRefreshLayout.visibility = View.VISIBLE
               binding.swipeRefreshLayout.isRefreshing = false
               Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
           }


       }
    }


    private fun initRecyclerView(){

        val adapter = OrderHistoryAdapter(this,viewModel.orders!!, ::onItemClick)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

    }


    private fun onItemClick(order:OrderModel){
        val intent = Intent(this, OrderActivity::class.java)
        intent.putExtra( "order_id",order._id)
        intent.putExtra( "restaurantId",order.restaurant_id)
        intent.putExtra( "isAfterOrdered",false)
        startActivity(intent)
    }


    private fun onSwipeRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            getOrders()
        }
    }



}