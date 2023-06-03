package com.example.mealmoverskotlin.domain.repositoryImpl

import android.util.Log
import com.example.mealmoverskotlin.data.apis.OrderApi
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.domain.firebase.FireStoreUseCase
import com.example.mealmoverskotlin.domain.repositorylnterfaces.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi,
    private val fireStore: FireStoreUseCase
) : OrderRepository {

     override suspend fun createNewOrder(order: OrderModel, callBack: (OrderModel?, Exception?) -> Unit) {
        try {

//            val apiRes =  orderApi.createOrder(order).awaitResponse()
//            if (apiRes.isSuccessful){
//
////                fireStore.sendNewOrder1(apiRes.body()!!){
////                    if (it == 200){
////                        callBack(apiRes.body(), null)
////                    }else{
////                        throw Exception("couldn't save order to firebase")
////                    }
////                }
//
//            }else{
//                throw Exception("couldn't save order to backend")
//            }


        }catch (e:Exception){
            callBack(null, e)
            Log.e("order repo create order", e.message, e)

        }






    }

    override suspend fun createNewOrder2(order: OrderModel): Flow<DataState<OrderModel>> = flow {
        emit(DataState.Loading)
        try {

            val apiRes =  orderApi.createOrder(order)
            fireStore.sendNewOrder(apiRes)
            emit(DataState.Success(apiRes))
        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("order repo create order", e.message, e)

        }
    }

    override suspend fun getOrdersFoUser(id: String): Flow<DataState<List<OrderModel>>> = flow {
        emit(DataState.Loading)
        try {
            val res = orderApi.getOrdersForUser(id)
            emit(DataState.Success(res))
        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("order repo create order", e.message, e)

        }
    }

    override suspend fun getOrderById(id: String, callBack: (OrderModel?, Exception?) -> Unit) {

    }
}