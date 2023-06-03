package com.example.mealmoverskotlin.domain.repositorylnterfaces

import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.shared.RetrofitInterface
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

   suspend fun createNewOrder(order:OrderModel, callBack: (OrderModel?, Exception?) -> Unit)
   suspend fun createNewOrder2(order:OrderModel) :Flow<DataState<OrderModel>>
   suspend fun getOrdersFoUser(id:String):Flow<DataState<List<OrderModel>>>
   suspend fun getOrderById(id:String, callBack: (OrderModel?, Exception?) -> Unit)


}