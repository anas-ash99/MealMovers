package com.example.mealmoverskotlin.domain.firebase

import android.util.Log
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.OrderModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.StructuredQuery.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FireStoreUseCase {
    private var db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("orders")

    suspend fun sendNewOrder(order:OrderModel):Flow<DataState<Any?>>  = flow{
        try {

             collectionRef.add(order).await()
             emit(DataState.Success(true))
        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("firebase", e.toString())
        }


    }

    suspend fun sendNewOrder1(order:OrderModel, statusCode: (Int) ->Unit)  {
        try {

            collectionRef.add(order).await()
            statusCode(200)
        }catch (e:Exception){
            statusCode(400)
            Log.e("firebase", e.toString())
        }


    }

}