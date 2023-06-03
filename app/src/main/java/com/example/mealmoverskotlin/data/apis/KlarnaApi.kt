package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.klarnaModels.KlarnaBody
import com.example.mealmoverskotlin.data.models.klarnaModels.KlarnaResponse
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface KlarnaApi {

    @Headers("Content-Type: application/json")
    @POST("/payments/v1/sessions")
     fun getKlarnaClientId(@Body body: KlarnaBody, @Header("Authorization") authHeader:String): Call<KlarnaResponse>

    @Headers("Content-Type: application/json")
    @POST("/payments/v1/sessions")
    suspend fun getKlarnaClientId1(@Body body: KlarnaBody, @Header("Authorization") authHeader:String): KlarnaResponse
}