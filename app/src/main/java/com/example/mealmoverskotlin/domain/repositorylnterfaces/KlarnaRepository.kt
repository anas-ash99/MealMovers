package com.example.mealmoverskotlin.domain.repositorylnterfaces

import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.dataStates.KlarnaState
import com.example.mealmoverskotlin.data.models.OrderModel
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentView
import kotlinx.coroutines.flow.Flow

interface KlarnaRepository {
    suspend fun createPayment(order: OrderModel) :Flow<DataState<String>>


}