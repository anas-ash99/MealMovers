package com.example.mealmoverskotlin.domain.repositoryImpl


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.mealmoverskotlin.data.apis.OrderApi
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.StripeResponse
import com.example.mealmoverskotlin.domain.repositorylnterfaces.StripeRepository
import com.example.mealmoverskotlin.shared.DataHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject


class StripeRepoImpl @Inject constructor (
    private val orderApi: OrderApi,
    private val context: Context
        ):StripeRepository {

    override suspend fun createPayment(amount:String): Flow<DataState<StripeResponse>> = flow {
        emit(DataState.Loading)

        try {
            val res = orderApi.createStripePayment(amount)
           emit(DataState.Success(res))
        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("stripe", e.message, e)
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
        }
    }

}