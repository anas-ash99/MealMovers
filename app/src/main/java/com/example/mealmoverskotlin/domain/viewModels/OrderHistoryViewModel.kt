package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.repositorylnterfaces.OrderRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.example.mealmoverskotlin.shared.DataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@SuppressLint("StaticFieldLeak")
class OrderHistoryViewModel @Inject constructor(
    private val repository: RestaurantRepositoryInterface,
    private val orderRepository: OrderRepository
) :ViewModel() {

    var orders:List<OrderModel>? = null
    val _orders by lazy {
        MutableLiveData<DataState<List<OrderModel>>>()
    }
   fun getOrders(){

        viewModelScope.launch {
            orderRepository.getOrdersFoUser(DataHolder.loggedInUser?._id!!).onEach {
                _orders.value = it
            }.launchIn(viewModelScope)
//            orderRepository.getOrdersFoUser(DataHolder.loggedInUser?._id!!, object : OnDone {
//                override fun onLoadingDone(result: Any?) {
//                    orders = result as List<OrderModel>
//                    callBack(orders, null)
//
//                }
//
//                override fun onError(e: Exception) {
//
//                    callBack(orders, null)
//
//                }
//            })
        }


    }





}