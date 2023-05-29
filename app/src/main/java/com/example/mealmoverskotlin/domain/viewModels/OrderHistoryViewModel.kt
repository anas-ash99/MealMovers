package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.example.mealmoverskotlin.shared.DataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@SuppressLint("StaticFieldLeak")
class OrderHistoryViewModel @Inject constructor(
    private val repository: RestaurantRepositoryInterface,
) :ViewModel() {

    var orders:List<OrderModel>? = null

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