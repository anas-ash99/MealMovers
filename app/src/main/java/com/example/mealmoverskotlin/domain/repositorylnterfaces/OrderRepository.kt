package com.example.mealmoverskotlin.domain.repositorylnterfaces

import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.shared.RetrofitInterface

interface OrderRepository {

   suspend fun createNewOrder(order:OrderModel, callBack: (OrderModel?, Exception?) -> Unit)
   suspend fun getOrdersFoUser(id:String, callBack: (List<OrderModel>?, Exception?) -> Unit)
   suspend fun getOrderById(id:String, callBack: (OrderModel?, Exception?) -> Unit)


}