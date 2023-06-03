package com.example.mealmoverskotlin.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import androidx.cardview.widget.CardView
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.domain.viewModels.OrderCheckoutPageViewModel
import com.example.mealmoverskotlin.shared.PaymentMethod

class PaymentMethodDialog(
    context: Context,
    private val viewModel: OrderCheckoutPageViewModel

): BaseDialog(context, Gravity.BOTTOM, R.layout.dialog_payment_method) {
    private val payPalCard:CardView =dialog.findViewById(R.id.paypalCard)
    private val cashCard:CardView =dialog.findViewById(R.id.cashCard)
    private val klarnaCard:CardView =dialog.findViewById(R.id.klarnaCard)
    private val creditCardCard:CardView =dialog.findViewById(R.id.creditCartCard)
    init {
        onClicksListener()
    }

    @SuppressLint("SetTextI18n")
    private fun onClicksListener() {
        payPalCard.setOnClickListener {
            viewModel.paymentMethod.value = PaymentMethod.PAYPAL
             dialog.dismiss()
        }
        creditCardCard.setOnClickListener {

            viewModel.paymentMethod.value = PaymentMethod.CREDIT_CARD
            dialog.dismiss()
        }
        klarnaCard.setOnClickListener {
            viewModel.paymentMethod.value = PaymentMethod.KLARNA
            dialog.dismiss()
        }
        cashCard.setOnClickListener {
            viewModel.paymentMethod.value = PaymentMethod.CASH
            dialog.dismiss()
        }
    }


}