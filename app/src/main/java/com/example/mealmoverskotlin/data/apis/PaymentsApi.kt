package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.StripeRes
import com.example.mealmoverskotlin.data.models.klarnaModels.KlarnaBody
import com.example.mealmoverskotlin.data.models.klarnaModels.KlarnaResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PaymentsApi {

    @POST("/v1/customers")
    fun getCustomerKey(@Header("Authorization") secret_key:String ) : Call<StripeRes>


    @POST("/v1/ephemeral_keys")
    fun getEphemeralKey(@Body customer: HashMap<String,String>,@Header("Authorization") secret_key:String, @Header("Stripe-Version") version:String ) : Call<StripeRes>




}