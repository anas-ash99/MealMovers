package com.example.mealmoverskotlin.domain.repositorylnterfaces

import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.StripeResponse
import kotlinx.coroutines.flow.Flow

interface StripeRepository {

    suspend fun createPayment(amount:String):Flow<DataState<StripeResponse>>

}