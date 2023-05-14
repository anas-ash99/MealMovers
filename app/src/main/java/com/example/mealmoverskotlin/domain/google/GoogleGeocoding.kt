package com.example.mealmoverskotlin.domain.google

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.mealmoverskotlin.data.apis.GoogleApi
import com.example.mealmoverskotlin.data.models.googleModls.GeoResGoogle
import com.example.mealmoverskotlin.data.models.googleModls.GoogleLatlngResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GoogleGeocoding (
    private val activity: Activity
        ) {
    private val API_KEY = "AIzaSyABajkttb898xIQBfmwcfXjw89SIRbP83o"
    private var retrofit:GoogleApi? = null
    private var instence:GoogleGeocoding? = null
     init {
         initRetrofit()
     }

    private fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/")
            .build()
            .create(GoogleApi::class.java)
    }
    fun getAddress(address:String, onDone: OnDone){
      try {

          retrofit?.getAddressByTextGoogle(address, API_KEY)?.enqueue(object : Callback<GeoResGoogle?> {
              override fun onResponse(
                  call: Call<GeoResGoogle?>,
                  response: Response<GeoResGoogle?>
              ) {
                  println(response)
                  onDone.onLoadingDone(response.body()!!)
              }

              override fun onFailure(call: Call<GeoResGoogle?>, t: Throwable) {
                  onDone.onError(Exception(t.message, t))
                  Log.e("google", t.message!!,t)
                  println(t)
              }
          })
      }catch (e:Exception){
          Log.e("Google", e.message, e)
          onDone.onError(e)
          println(e)
      }


    }
    fun getAddressByLatlng(latlng:String, onDone: OnDone){
        try {

            retrofit?.getAddressByLatlng(latlng , API_KEY)?.enqueue(object : Callback<GoogleLatlngResponse?> {
                override fun onResponse(
                    call: Call<GoogleLatlngResponse?>,
                    response: Response<GoogleLatlngResponse?>
                ) {
                    println(response)
                    onDone.onLoadingDone(response.body())
                }

                override fun onFailure(call: Call<GoogleLatlngResponse?>, t: Throwable) {
                    onDone.onError(Exception(t.message, t))
                    Log.e("google", t.message!!,t)
                    println(t)
                }
            })
        }catch (e:Exception){
            Log.e("Google", e.message, e)
            onDone.onError(e)
            println(e)
        }


    }

}


interface OnDone{
    fun onLoadingDone(res:Any?)
    fun onError(e:Exception)

}