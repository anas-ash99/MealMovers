package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.StripeResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApi {

    @POST("/order/create_new_order")
    suspend fun createOrder(@Body order: OrderModel): OrderModel
    @POST("/order/stripe_payment/{amount}")
    suspend fun createStripePayment(@Path("amount") amount: String): StripeResponse

    @POST("/order/create_new_order")
    suspend fun createOrder1(@Body order: OrderModel): Call<OrderModel>

    @GET("/order/get_order_by_id/{id}")
    suspend fun getOrderById(@Path("id") orderId:String): OrderModel


    @GET("/order/get_orders_for_user/{id}")
    suspend fun getOrdersForUser(@Path("id") orderId:String): List<OrderModel>


}