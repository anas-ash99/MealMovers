package com.example.mealmoverskotlin.domain.repositoryImpl

import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import com.example.mealmoverskotlin.BuildConfig
import com.example.mealmoverskotlin.data.apis.KlarnaApi
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.dataStates.KlarnaState
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.klarnaModels.KlarnaBody
import com.example.mealmoverskotlin.data.models.klarnaModels.OrderLine
import com.example.mealmoverskotlin.domain.repositorylnterfaces.KlarnaRepository
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentCategory
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentView
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentViewCallback
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentsSDKError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KlarnaRepoImpl @Inject constructor (
    private val klarnaApi: KlarnaApi
): KlarnaRepository {

     override suspend fun createPayment(order: OrderModel): Flow<DataState<String>>  = flow {
        emit(DataState.Loading)
        try {

            val res = klarnaApi.getKlarnaClientId1(getKlarnaBody(order), getHeader())
            emit(DataState.Success(res.client_token!!))

        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("klarna retrofit",e.message, e )
        }

    }


    private fun getHeader():String{
        val base:String = BuildConfig.KLARNA_USERNAME + ":" + BuildConfig.KLARNA_PASSWORD
        return "Basic " + Base64.encodeToString(base.encodeToByteArray(), Base64.NO_WRAP)
    }
    private fun getKlarnaBody(order:OrderModel):KlarnaBody{
        val klarnaBody = KlarnaBody()
        val totalAmount = PriceTrimmer.trim(order.orderPrice + DataHolder.restaurant.deliveryPrice.toDouble()).replace(".", "").toInt()
        klarnaBody.order_lines.add(OrderLine())
        klarnaBody.order_amount= totalAmount
        klarnaBody.order_lines[0].unit_price = totalAmount
        klarnaBody.order_lines[0].total_amount= totalAmount
        return klarnaBody
    }
}