package com.example.mealmoverskotlin.domain.repositorylnterfaces

import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.OrderModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {


   suspend fun createNewOrder(order:OrderModel) :Flow<DataState<OrderModel>>
   suspend fun getOrdersFoUser(id:String):Flow<DataState<List<OrderModel>>>
   suspend fun getOrderById(id:String, callBack: (OrderModel?, Exception?) -> Unit)



}