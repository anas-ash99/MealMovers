package com.example.mealmoverskotlin.domain.stripe


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mealmoverskotlin.BuildConfig
import com.example.mealmoverskotlin.data.apis.PaymentsApi
import com.example.mealmoverskotlin.data.models.StripeRes
import com.example.mealmoverskotlin.shared.Constants
import com.example.mealmoverskotlin.shared.DataHolder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class StripeUseCase (
    private val activity: ComponentActivity
        ) {
    var amount:String = ""
    var customerID: String? = null
    var paymentSheet: PaymentSheet? = null
    var ephemeral_key: String? = null
    var client_secret: String? = null
    var loggedInUser = DataHolder.loggedInUser
    val paymentSheetLoading : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()

    }

    val paymentResult : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val retrofit:PaymentsApi
    init {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.stripe.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentsApi::class.java)
        paymentSheet = PaymentSheet(activity, ::onPaymentResult)
        PaymentConfiguration.init(activity, BuildConfig.STRIPE_PUBLISH_KEY)

    }


    fun createNewPayment() {
        println(loggedInUser)
        if (loggedInUser?.stripeCustomerId == ""){
            getCustomerId()
        }else{
            customerID = loggedInUser?.stripeCustomerId
            getEphemeralKey()
        }
    }
    fun getCustomerId(){
        retrofit.getCustomerKey("Bearer ${BuildConfig.STRIPE_SECRET_KEY}").enqueue(object : Callback<StripeRes?> {
            override fun onResponse(call: Call<StripeRes?>, response: Response<StripeRes?>) {

                if(response.isSuccessful){
                   customerID = response.body()?.id
                    getEphemeralKey()
                    loggedInUser?.stripeCustomerId = customerID!!

                }else{
                    Toast.makeText(activity, "couldn't get customer id", Toast.LENGTH_SHORT).show()
                    paymentSheetLoading.value = false
                }

            }

            override fun onFailure(call: Call<StripeRes?>, t: Throwable) {
                Log.e("Stripe", t.toString())
                paymentSheetLoading.value = false
            }
        })

    }

    fun getEphemeralKey() {
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            "https://api.stripe.com/v1/ephemeral_keys",
            com.android.volley.Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    ephemeral_key = jsonObject.getString("id")
                    getClientSecret()
                } catch (e: JSONException) {
                    Log.e("MYAPP",  e.toString())
                    paymentSheetLoading.value = false
                }
            }, com.android.volley.Response.ErrorListener { error ->
                Log.e("error", error.toString())
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                paymentSheetLoading.value = false
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = java.util.HashMap()
                header["Authorization"] = "Bearer ${BuildConfig.STRIPE_SECRET_KEY}"
                header["Stripe-Version"] = "2022-11-15"
                return header
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val params: MutableMap<String, String> = java.util.HashMap()
                params["customer"] = customerID!!
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(stringRequest)

    }

    private fun getClientSecret() {
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            "https://api.stripe.com/v1/payment_intents",
            com.android.volley.Response.Listener { response ->

                val jsonObject = JSONObject(response)
                client_secret = jsonObject.getString("client_secret")
                paymentFlow()
            }, com.android.volley.Response.ErrorListener { error ->
                paymentSheetLoading.value = false
                Log.e("error", error.toString())
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val header: MutableMap<String, String> = HashMap()
                header["Authorization"] = "Bearer ${BuildConfig.STRIPE_SECRET_KEY}"
                return header
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["customer"] = customerID!!
                params["amount"] = amount!!
                params["currency"] = "eur"
                params["automatic_payment_methods[enabled]"] = "true"
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(stringRequest)
    }

    private fun paymentFlow() {


        paymentSheet?.presentWithPaymentIntent(
            client_secret!!,
            PaymentSheet.Configuration("MealMovers", PaymentSheet.CustomerConfiguration(
            customerID!!,
            ephemeral_key!!
        )))
        paymentSheetLoading.value = false
    }
    private fun onPaymentResult(paymentSheetResult: PaymentSheetResult) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            paymentResult.value = "PAID"
            println(222)
        }else{
            paymentResult.value = "CANCEL"
            paymentSheetLoading.value  = true
        }
    }

}