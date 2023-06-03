package com.example.mealmoverskotlin.ui.restaurant_page

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.BuildConfig
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.StripeResponse
import com.example.mealmoverskotlin.databinding.ActivityConfirmOrderBinding
import com.example.mealmoverskotlin.domain.payments.PayPal
import com.example.mealmoverskotlin.domain.viewModels.OrderCheckoutPageViewModel
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.PaymentMethod
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
import com.example.mealmoverskotlin.ui.address.AddressActivity
import com.example.mealmoverskotlin.ui.dialogs.AddressFillingDialog
import com.example.mealmoverskotlin.ui.dialogs.DeliveryTimeDialog
import com.example.mealmoverskotlin.ui.dialogs.PaymentMethodDialog
import com.example.mealmoverskotlin.ui.order.OrderCompletedActivity
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentCategory
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentView
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentViewCallback
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentsSDKError
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint



@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
@AndroidEntryPoint
class ConfirmOrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityConfirmOrderBinding
    private lateinit var stripePaymentSheet:PaymentSheet

    private val viewModel:OrderCheckoutPageViewModel by viewModels()
    private val paymentMethodDialog by lazy { PaymentMethodDialog(this@ConfirmOrderActivity, viewModel) }
    private val addressDialog by lazy { AddressFillingDialog(this@ConfirmOrderActivity, viewModel) }
    private val deliveryTimeDialog by lazy { DeliveryTimeDialog(this@ConfirmOrderActivity, viewModel) }
    private val payPal by lazy { PayPal(this@ConfirmOrderActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_confirm_order)
        viewModel.order = intent.getSerializableExtra("order") as OrderModel
        initFunctions()

    }

    private fun initPaymentSheets() {
        binding.paymentView.category = KlarnaPaymentCategory.PAY_LATER
        stripePaymentSheet =  PaymentSheet(this@ConfirmOrderActivity,::onPaymentResult)
        PaymentConfiguration.init(this, BuildConfig.STRIPE_PUBLISH_KEY)
    }

    private fun initFunctions(){
        initPaymentSheets()
        onArrowBackClick()
        observeKlarnaCreatePayment()
        onContinueKlarnaClick()
        onPayButtonClick()
        observeCreateNewOrder()
        initPageValues()
        observePaymentMethod()
        observeStripePayment()
        handleCardsLayoutClick()
        observeDeliverTime()
        observeUserAddress()
    }

    private fun observeUserAddress() {
        viewModel.userAddress.observe(this){
            it?.let { binding.address = it  }
        }
    }

    private fun observeDeliverTime() {
        viewModel.deliveryTime.observe(this){
            binding.deliveryTimeTV.text = it
        }
    }

    private fun handleCardsLayoutClick() {
        binding.addressCard.setOnClickListener {
//            addressDialog.dialog.show()
            val i = Intent(this,AddressActivity::class.java)
            i.putExtra("isFromCheckOutPage", true)
            startActivity(i)
        }
        binding.paymentCard.setOnClickListener {
            paymentMethodDialog.dialog.show()
        }
        binding.timeCard.setOnClickListener {
            deliveryTimeDialog.showDialog()
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.userAddress.value = DataHolder.userAddress
    }
    @SuppressLint("SetTextI18n")
    private fun observePaymentMethod() {
        viewModel.paymentMethod.observe(this){
            when(it){
                PaymentMethod.PAYPAL -> {
                    binding.textPaymentMethod.text = "PayPal"
                    binding.paymentMethodImage.setBackgroundResource(R.drawable.logo_paypal_icon)

                }
                PaymentMethod.KLARNA -> {
                    binding.textPaymentMethod.text = "Klarna"
                    binding.paymentMethodImage.setBackgroundResource(R.drawable.klarna_logo)
                }
                PaymentMethod.CASH -> {
                    binding.textPaymentMethod.text = "Cash"
                    binding.paymentMethodImage.setBackgroundResource(R.drawable.cash_money_icon)
                }
                PaymentMethod.CREDIT_CARD -> {
                    binding.textPaymentMethod.text = "Credit card"
                    binding.paymentMethodImage.setBackgroundResource(R.drawable.credit_card_icon)
                }
            }
        }
    }

    private fun observeCreateNewOrder() {
        viewModel._createNewOrder.observe(this){
            when(it){
                is DataState.Error -> {
                    Toast.makeText(this, "something went wrong creating the order", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                DataState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is DataState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.order = it.data
                    viewModel.updateUserAddress()
                    viewModel.updateLoggedInUser()
                    startTrackOrderActivity()
                }
            }
        }
    }

    private fun onPaymentResult(paymentSheetResult: PaymentSheetResult) {
        if (paymentSheetResult is PaymentSheetResult.Completed) {
            viewModel.createNewOrder()
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun stripePaymentFlow(response: StripeResponse) {


        stripePaymentSheet.presentWithPaymentIntent(
            response.paymentIntent,
            PaymentSheet.Configuration("MealMovers", PaymentSheet.CustomerConfiguration(
                response.customer,
                response.ephemeralKey
            )))

    }

    private fun observeStripePayment() {
        viewModel._stripeCreatePayment.observe(this){
            when(it){
                is DataState.Error ->{binding.progressBar.visibility = View.GONE}
                is DataState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    stripePaymentFlow(it.data)
                }
                DataState.Loading -> binding.progressBar.visibility = View.VISIBLE
            }
        }
    }


    private fun onPayButtonClick(){
        binding.payButton.setOnClickListener {

            if (viewModel.validatedUserAddress()){
                when(viewModel.paymentMethod.value){
                    PaymentMethod.CREDIT_CARD ->{
                        viewModel.createStripePayment()
                    }
                    PaymentMethod.CASH ->{
                        viewModel.createNewOrder()
                    }
                    PaymentMethod.KLARNA -> {
                        binding.progressBar.visibility = View.VISIBLE
                        viewModel._klarnaCreatePayment.value = null
                        viewModel.createKlarnaPayment()
                    }
                    PaymentMethod.PAYPAL -> payPal.startPayment(viewModel.order.orderPrice.toString())

                    else -> {}
                }
            }else{
                Toast.makeText(this, "Please complete your address", Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun observeKlarnaCreatePayment() {
        viewModel._klarnaCreatePayment.observe(this){
            when(it){
                is DataState.Error -> {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE

                }
                DataState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is DataState.Success -> {
                    initKlarnaView(it.data)

                }
            }

        }
    }


    private fun onContinueKlarnaClick(){
        binding.payButtonKlarna.setOnClickListener {
            binding.paymentView.authorize(false, null)
        }
    }


    @SuppressLint("MissingPermission")
    private fun initKlarnaView(clientToken:String){

        binding.paymentView.initialize(clientToken, "host.com")
        binding.paymentView.registerPaymentViewCallback(  object : KlarnaPaymentViewCallback {
            override fun onAuthorized(
                view: KlarnaPaymentView,
                approved: Boolean,
                authToken: String?,
                finalizedRequired: Boolean?
            ) {
                if (approved){

                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.createNewOrder()
                }else{

                    binding.progressBar.visibility = View.GONE
                    binding.klarnaLayout.visibility = View.GONE
                    binding.mainLayout.visibility = View.VISIBLE
                }
            }

            override fun onErrorOccurred(view: KlarnaPaymentView, error: KlarnaPaymentsSDKError) {
                Toast.makeText(this@ConfirmOrderActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onFinalized(
                view: KlarnaPaymentView,
                approved: Boolean,
                authToken: String?
            ) {

            }

            override fun onInitialized(view: KlarnaPaymentView) {
                view.load(null)
            }

            override fun onLoadPaymentReview(view: KlarnaPaymentView, showForm: Boolean) {

            }

            override fun onLoaded(view: KlarnaPaymentView) {
                binding.klarnaLayout.visibility = View.VISIBLE
                binding.mainLayout.visibility =View.GONE
                binding.progressBar.visibility = View.GONE
            }

            override fun onReauthorized(
                view: KlarnaPaymentView,
                approved: Boolean,
                authToken: String?
            ) {

            }
        })

    }




    private fun onArrowBackClick() {
        binding.backArrow.setOnClickListener {

            onBackPressed()
        }
    }



    override fun onBackPressed() {
        if (binding.klarnaLayout.visibility == View.VISIBLE){
            binding.progressBar.visibility = View.GONE
            binding.klarnaLayout.visibility = View.GONE
            binding.mainLayout.visibility = View.VISIBLE
        }else{
            super.onBackPressed()

        }
    }

    private fun startTrackOrderActivity(){
        val intent = Intent(this, OrderCompletedActivity::class.java)
        intent.putExtra( "order_id",viewModel.order._id)
        intent.putExtra( "restaurantId",DataHolder.restaurant._id)
        startActivity(intent)
        finish()
    }
    @SuppressLint("SetTextI18n")
    private fun initPageValues() {

        binding.totalPrice.text = (viewModel.order.orderPrice + DataHolder.restaurant.deliveryPrice.toDouble()).trim1() + "€"
        binding.deliveryFee.text = viewModel.restaurant.deliveryPrice + "€"
        binding.itemsTotal.text = (viewModel.order.orderPrice).trim1() + "€"

    }



}