package com.example.mealmoverskotlin.domain.repositoryImpl

import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.example.mealmoverskotlin.data.apis.OrderApi
import com.example.mealmoverskotlin.data.apis.RestaurantsApi
import com.example.mealmoverskotlin.data.apis.UserApi
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.events.AddRestaurantToFavouritesEvent
import com.example.mealmoverskotlin.data.models.*
import com.example.mealmoverskotlin.shared.RetrofitInterface
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor (
   private val restaurantsApi: RestaurantsApi,
   private val orderApi: OrderApi,

): RestaurantRepositoryInterface {
    override suspend fun getAllRestaurants(): Flow<DataState<MutableList<RestaurantModel>>> = flow {
        emit(DataState.Loading)
        try {
            val res =  restaurantsApi.getAllRestaurants().awaitResponse()
            if (res.isSuccessful){
                emit(DataState.Success(res.body()!!))
            }else{
                throw Exception(res.message())

            }


        }catch (e:Exception){
            Log.e("restaurants", e.toString())
            emit(DataState.Error(e))
        }

    }




    override suspend fun getOrderById(id: String, onLoadingDone: RetrofitInterface) {
        try {
            orderApi.getOrderById(id).enqueue(object : Callback<OrderModel?> {
                override fun onResponse(call: Call<OrderModel?>, response: Response<OrderModel?>) {
                    onLoadingDone.onSuccess(response.body()!!)
                }

                override fun onFailure(call: Call<OrderModel?>, t: Throwable) {
                    onLoadingDone.onError(Exception(t.message, t))
                }
            })
        }catch (e:Exception){
            onLoadingDone.onError(e)
            Log.e("order", e.message!!,e )
        }
    }

    override suspend fun getRestaurantById(id: String, onLoadingDone: RetrofitInterface) {
        try {
            restaurantsApi.getRestaurantById(id).enqueue(object : Callback<RestaurantModel?> {
                override fun onResponse(
                    call: Call<RestaurantModel?>,
                    response: Response<RestaurantModel?>
                ) {
                    onLoadingDone.onSuccess(response.body()!!)
                }

                override fun onFailure(call: Call<RestaurantModel?>, t: Throwable) {
                    onLoadingDone.onError(Exception(t.message, t))
                }
            })
        }catch (e:Exception){
            onLoadingDone.onError(e)
            Log.e("order", e.message!!,e )
        }
    }


    override suspend fun getAllRestaurants2(callBack: (restaurants: List<RestaurantModel>?, error: Exception?) -> Unit) {
        try {
            val res =  restaurantsApi.getAllRestaurants().awaitResponse()
            if (res.isSuccessful){
                 callBack(res.body()!!, null)
            }else{
                throw Exception(res.message())

            }

        }catch (e:Exception){
            Log.e("restaurants", e.message!!,e )
            callBack(null,e)
        }
    }






}