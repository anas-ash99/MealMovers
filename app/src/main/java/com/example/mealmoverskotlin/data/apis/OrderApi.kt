package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.OrderModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApi {

    @POST("/order/create_new_order")
    fun createOrder(@Body order: OrderModel): Call<OrderModel>

    @GET("/order/get_order_by_id/{id}")
    fun getOrderById(@Path("id") orderId:String): Call<OrderModel>


    @GET("/order/get_orders_for_user/{id}")
    fun getOrdersForUser(@Path("id") orderId:String): Call<List<OrderModel>>
}