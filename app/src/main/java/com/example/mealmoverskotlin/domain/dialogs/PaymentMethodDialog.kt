package com.example.mealmoverskotlin.domain.dialogs

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

    private fun onClicksListener() {
        payPalCard.setOnClickListener {
            viewModel.binding.textPaymentMethod.text = "PayPal"
            viewModel.binding.paymentMethodImage.setBackgroundResource(R.drawable.logo_paypal_icon)
            viewModel.paymentMethod = PaymentMethod.PAYPAL
             dialog.dismiss()
        }
        creditCardCard.setOnClickListener {
            viewModel.binding.textPaymentMethod.text = "Credit card"
            viewModel.binding.paymentMethodImage.setBackgroundResource(R.drawable.credit_card_icon)
            viewModel.paymentMethod = PaymentMethod.CREDIT_CARD
            dialog.dismiss()
        }
        klarnaCard.setOnClickListener {
            viewModel.binding.textPaymentMethod.text = "Klarna"
            viewModel.binding.paymentMethodImage.setBackgroundResource(R.drawable.klarna_logo)
            viewModel.paymentMethod = PaymentMethod.KLARNA
            dialog.dismiss()
        }
        cashCard.setOnClickListener {
            viewModel.binding.textPaymentMethod.text = "Cash"
            viewModel.binding.paymentMethodImage.setBackgroundResource(R.drawable.cash_money_icon)
            viewModel.paymentMethod = PaymentMethod.CASH
            dialog.dismiss()
        }
    }


}