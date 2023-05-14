package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.geoapifyModels.GeoapifyModel

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.mealmoverskotlin.data.models.geoapifyModels.Result
import com.example.mealmoverskotlin.data.models.geoapifyModels.ResultsModel

interface AddressApi {


    @GET("/v1/geocode/autocomplete/")
    fun getAddresses(@Query("text") text:String,
                     @Query("format")  format:String,
                     @Query("apiKey") apiKey:String,
                     @Query("limit") limit:Int,
                     @Query("type")  type:String) :Call<GeoapifyModel>



//    https://api.geoapify.com/v1/geocode/reverse?lat=50.73873873873874&lon=7.089252072373821&format=json&apiKey=YOUR_API_KEY
    @GET("/v1/geocode/reverse")
    fun getAddressByLonAndLat(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("format")  format:String,
        @Query("apiKey") apiKey:String,
    ) :Call<ResultsModel>
}