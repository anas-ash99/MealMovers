package com.example.mealmoverskotlin.domain.geoapify

import android.app.Activity
import android.util.Log
import com.example.mealmoverskotlin.data.apis.AddressApi
import com.example.mealmoverskotlin.data.models.geoapifyModels.GeoapifyModel
import com.example.mealmoverskotlin.data.models.geoapifyModels.ResultsModel
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class Geoapify(
    private val activity: Activity

) {
    private val API_KEY ="b7bfe07955f2474c9abd1e259b760fd2"
    private val url = "https://api.geoapify.com"


    private fun apiCall(): AddressApi{
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AddressApi::class.java)
    }




    fun getAddress(text:String, limit:Int, onLoadingDone:AutoCompleteAddress ){

        try {
           apiCall().getAddresses(text, "json", API_KEY, limit,"amenity").enqueue(object : Callback<GeoapifyModel?> {
                override fun onResponse(
                    call: Call<GeoapifyModel?>,
                    response: Response<GeoapifyModel?>
                ) {
                    onLoadingDone.onLoadingDone(response.body()!!)
                }

                override fun onFailure(call: Call<GeoapifyModel?>, t: Throwable) {
                    onLoadingDone.onFailure(Exception(t.message, t))
                    Log.e("order", t.cause.toString() )
                }
            })

        }catch (e:Exception){

            onLoadingDone.onFailure(e)
            Log.e("order", e.message!!,e )
        }



    }


    suspend fun getAddressByLatlng(lat:Double, lon:Double, getAddress: GetAddressByLatlng ){
        try {
            val res = apiCall().getAddressByLonAndLat(lat, lon, "json", API_KEY).awaitResponse()
            val body = res.body()
            getAddress.onLoadingDone(body!!)

        }catch (e:Exception){

            Log.e("geoapiky", e.toString())
            getAddress.onFailure(e)
        }

    }




}


interface GetAddressByLatlng{
    fun onLoadingDone(address:ResultsModel)
    fun onFailure(e:Exception)
}

interface AutoCompleteAddress{
    fun onLoadingDone(result: GeoapifyModel)
    fun onFailure(e:Exception)

}