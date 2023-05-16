package com.example.mealmoverskotlin.domain.viewModels

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.databinding.ActivityOrdersHistoryBinding
import com.example.mealmoverskotlin.domain.adapters.OrderHistoryAdapter
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.ui.order.OrdersHistoryActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val repository: MainRepositoryInterface
) :ViewModel() {

    private var orders:List<OrderModel>? = null
    private var adapter:OrderHistoryAdapter? = null
    private lateinit var activity: OrdersHistoryActivity
    private lateinit var binding:ActivityOrdersHistoryBinding

    fun init(activity: OrdersHistoryActivity, binding:ActivityOrdersHistoryBinding){
        this.binding = binding
        this.activity = activity
        binding.progressBar.visibility = View.VISIBLE
        binding.swipeRefreshLayout.visibility = View.GONE
        getOrders()
        onSwipeRefresh()
        onArrowBackClick()
    }


    private fun getOrders(){

        viewModelScope.launch {
            repository.getOrdersFoUser(DataHolder.loggedInUser?._id!!, object : OnDone {
                override fun onLoadingDone(result: Any?) {
                    orders = result as List<OrderModel>
                    initRecyclerView()
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.visibility = View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                override fun onError(e: Exception) {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.visibility = View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }



    private fun onArrowBackClick(){
        binding.arrowBack.setOnClickListener {
            activity.onBackPressed()
        }
    }
   private fun initRecyclerView(){
       adapter = OrderHistoryAdapter(activity, orders!!)
       binding.recyclerview.adapter = adapter
       binding.recyclerview.layoutManager = LinearLayoutManager(activity)

   }

    private fun onSwipeRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            getOrders()
        }
    }


}