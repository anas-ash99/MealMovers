package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.RestaurantModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantsApi {


    @GET("/restaurants/get_all_restaurants")
    fun getAllRestaurants(): Call<MutableList<RestaurantModel>>

    @GET("/restaurants/get_restaurant_by_id/{id}")
    fun getRestaurantById(@Path("id") id:String): Call<RestaurantModel>
}