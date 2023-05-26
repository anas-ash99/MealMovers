package com.example.mealmoverskotlin.domain.repositoryImpl

import android.util.Log
import com.example.mealmoverskotlin.data.apis.OrderApi
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.domain.firebase.FireStoreUseCase
import com.example.mealmoverskotlin.domain.repositorylnterfaces.OrderRepository
import retrofit2.awaitResponse
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi,
    private val fireStore: FireStoreUseCase
) : OrderRepository {
     override suspend fun createNewOrder(order: OrderModel, callBack: (OrderModel?, Exception?) -> Unit) {
        try {

            val apiRes =  orderApi.createOrder(order).awaitResponse()
            if (apiRes.isSuccessful){

                fireStore.sendNewOrder1(apiRes.body()!!){
                    if (it == 200){
                        callBack(apiRes.body(), null)
                    }else{
                        throw Exception("couldn't save order to firebase")
                    }
                }

            }else{
                throw Exception("couldn't save order to backend")
            }


        }catch (e:Exception){
            callBack(null, e)
            Log.e("order repo create order", e.message, e)

        }






    }

    override suspend fun getOrdersFoUser(id: String, callBack: (List<OrderModel>?, Exception?) -> Unit) {


    }

    override suspend fun getOrderById(id: String, callBack: (OrderModel?, Exception?) -> Unit) {

    }
}