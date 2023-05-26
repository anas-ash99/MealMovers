package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.databinding.ActivityOrdersHistoryBinding
import com.example.mealmoverskotlin.domain.adapters.OrderHistoryAdapter
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.ui.order.OrdersHistoryActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.firestore.v1.StructuredQuery.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@SuppressLint("StaticFieldLeak")
class OrderHistoryViewModel @Inject constructor(
    private val repository: MainRepositoryInterface,
    private val context: Context
) :ViewModel() {

    var orders:List<OrderModel>? = null
    private var adapter:OrderHistoryAdapter? = null
    private lateinit var binding:ActivityOrdersHistoryBinding

   fun getOrders(callBack: (orders:List<OrderModel>?, error:Exception?) -> Unit){

        viewModelScope.launch {
            repository.getOrdersFoUser(DataHolder.loggedInUser?._id!!, object : OnDone {
                override fun onLoadingDone(result: Any?) {
                    orders = result as List<OrderModel>
                    callBack(orders, null)

                }

                override fun onError(e: Exception) {

                    callBack(orders, null)

                }
            })
        }


    }





}