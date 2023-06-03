package com.example.mealmoverskotlin.ui.restaurant_page

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.mealmoverskotlin.BuildConfig
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.StripeResponse
import com.example.mealmoverskotlin.domain.viewModels.OrderCheckoutPageViewModel
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.PaymentMethod
import com.example.mealmoverskotlin.ui.order.OrderCompletedActivity
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentCategory
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentView
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentViewCallback
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentsSDKError
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class ConfirmOrderActivity : AppCompatActivity() {
    private lateinit var binding:com.example.mealmoverskotlin.databinding.ActivityConfirmOrderBinding
    private lateinit var stripePaymentSheet:PaymentSheet
    private val viewModel:OrderCheckoutPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_confirm_order)
        viewModel.init(binding, this)
        viewModel.initDialog()
        onArrowBackClick()
        observeKlarnaCreatePayment()
        onContinueKlarnaClick()
        onPayButtonClick()
        observeCreateNewOrder()
        stripePaymentSheet = PaymentSheet(this,::onPaymentResult)
        binding.paymentView.category = KlarnaPaymentCategory.PAY_LATER
        observeStripePayment()
        PaymentConfiguration.init(this, BuildConfig.STRIPE_PUBLISH_KEY)
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
                when(viewModel.paymentMethod){
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
                    PaymentMethod.PAYPAL -> viewModel.payPal.startPayment(viewModel.order.orderPrice.toString())

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




}