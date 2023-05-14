package com.example.mealmoverskotlin.domain

import android.content.Intent
import com.example.mealmoverskotlin.shared.PayPalConst
import com.example.mealmoverskotlin.ui.restaurant_page.ConfirmOrderActivity
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import java.math.BigDecimal

class PayPal (
    private val activity:ConfirmOrderActivity
        ){
    private val config:PayPalConfiguration = PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId(PayPalConst.CLIENT_ID)



    fun startPayment(amount:String){
        val payment = PayPalPayment(BigDecimal(amount), "USD", "ONLINE ORDER", PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(activity, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        activity.startActivityForResult(intent, PayPalConst.PAYPAL_REQUEST_CODE)

    }

    fun onActivityResults(){



    }



}