package com.example.mealmoverskotlin.domain.google

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.mealmoverskotlin.BuildConfig
import com.example.mealmoverskotlin.data.apis.GoogleApi
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.googleModls.GeoResGoogle
import com.example.mealmoverskotlin.data.models.googleModls.GoogleLatlngResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GoogleGeocoding(
    private val retrofit: GoogleApi
) {

//    private var retrofit:GoogleApi? = null
//     init {
//         initRetrofit()
//     }
//
//    private fun initRetrofit() {
//        retrofit = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl("https://maps.googleapis.com/")
//            .build()
//            .create(GoogleApi::class.java)
//    }
    fun getAddress(address:String, callBack:(GeoResGoogle?, Exception?) -> Unit){
        retrofit.getAddressByTextGoogle(address, BuildConfig.MAPS_API_KEY).enqueue(object : Callback<GeoResGoogle?> {
              override fun onResponse(
                  call: Call<GeoResGoogle?>,
                  response: Response<GeoResGoogle?>
              ) {

                  callBack(response.body(), null)
//                  callBack(DataState.Success(response.body()!!))
              }

              override fun onFailure(call: Call<GeoResGoogle?>, t: Throwable) {
//                  callBack(DataState.Error(Exception(t.message,t)))
                  callBack(null,Exception(t.message,t) )
                  Log.e("google", t.message!!,t)

              }
        })

    }
    fun getAddressByLatlng(latlng:String, onDone: OnDone){
        try {

            retrofit?.getAddressByLatlng(latlng , BuildConfig.MAPS_API_KEY)?.enqueue(object : Callback<GoogleLatlngResponse?> {
                override fun onResponse(
                    call: Call<GoogleLatlngResponse?>,
                    response: Response<GoogleLatlngResponse?>
                ) {
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