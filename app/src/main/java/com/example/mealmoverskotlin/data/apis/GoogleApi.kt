package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.googleModls.GeoResGoogle
import com.example.mealmoverskotlin.data.models.googleModls.GoogleLatlngResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {

    @GET("maps/api/geocode/json")
    fun getAddressByTextGoogle(@Query("address") address:String, @Query("key") api_key:String): Call<GeoResGoogle>
    @GET("maps/api/geocode/json")
    fun getAddressByLatlng(@Query("latlng") latlng:String,  @Query("key") api_key:String): Call<GoogleLatlngResponse>


}