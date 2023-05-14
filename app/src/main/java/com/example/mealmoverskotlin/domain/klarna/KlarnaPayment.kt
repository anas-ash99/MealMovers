package com.example.mealmoverskotlin.domain.klarna

import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mealmoverskotlin.data.apis.KlarnaApi
import com.example.mealmoverskotlin.data.apis.PaymentsApi
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.klarnaModels.KlarnaBody
import com.example.mealmoverskotlin.data.models.klarnaModels.KlarnaResponse
import com.example.mealmoverskotlin.data.models.klarnaModels.OrderLine
import com.example.mealmoverskotlin.databinding.ActivityConfirmOrderBinding
import com.example.mealmoverskotlin.domain.viewModels.ConfirmOrderPageViewModel
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.KlarnaConst
import com.example.mealmoverskotlin.shared.PriceTrimmer
import com.example.mealmoverskotlin.ui.restaurant_page.ConfirmOrderActivity
import com.google.gson.JsonObject
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentCategory
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentView
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentViewCallback
import com.klarna.mobile.sdk.api.payments.KlarnaPaymentsSDKError
import com.klarna.mobile.sdk.payments.PaymentView
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Inject


class KlarnaPayment(
    private val activity: ConfirmOrderActivity,
    private val binding:ActivityConfirmOrderBinding,
    private val viewModel: ConfirmOrderPageViewModel
) : KlarnaPaymentViewCallback{

    @Inject
    lateinit var api:KlarnaApi
    var CLIENT_TOKEN:String? = null
    var totalAmount:Int = 0
    var base:String = KlarnaConst.USERNAME + ":" + KlarnaConst.PASSWORD
    var klarnaBody: KlarnaBody = KlarnaBody()
    var authHeader ="Basic " + Base64.encodeToString(base.encodeToByteArray(), Base64.NO_WRAP)
    val paymentView:KlarnaPaymentView = binding.paymentView
    init {
        klarnaBody.order_lines.add(OrderLine())
        paymentView.category = KlarnaPaymentCategory.PAY_LATER

    }

    fun createPayment(order:OrderModel){
        if (CLIENT_TOKEN == null){
            createSession(order)

        }else{
            loadView()
        }
    }

    fun createSession(order:OrderModel){
        totalAmount = PriceTrimmer.trim(order.orderPrice + DataHolder.restaurant.deliveryPrice.toDouble()).replace(".", "").toInt()
        klarnaBody.order_amount= totalAmount
        klarnaBody.order_lines[0].unit_price = totalAmount
        klarnaBody.order_lines[0].total_amount= totalAmount
        retrofit().getKlarnaClientId(klarnaBody, authHeader).enqueue(object : Callback<KlarnaResponse?> {
            override fun onResponse(
                call: Call<KlarnaResponse?>,
                response: Response<KlarnaResponse?>
            ) {
                if (response.isSuccessful){
                    CLIENT_TOKEN = response.body()?.client_token
                    loadView()
                }else if (response.code() == 400){
                    Toast.makeText(activity, "BAD VALUE", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility =View.GONE
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<KlarnaResponse?>, t: Throwable) {
                Log.e("klarna", t.toString())
                binding.progressBar.visibility =View.GONE
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()

            }
        })

    }
    fun loadView(){

        paymentView.initialize(CLIENT_TOKEN!!, "host.com")
        paymentView.registerPaymentViewCallback(this)
//        paymentView.registerPaymentViewCallback(object : KlarnaPaymentViewCallback {
//            override fun onAuthorized(
//                view: KlarnaPaymentView,
//                approved: Boolean,
//                authToken: String?,
//                finalizedRequired: Boolean?
//            ) {
//                if (approved){
//                    Toast.makeText(activity, "Approved", Toast.LENGTH_SHORT).show()
//                    CLIENT_TOKEN = null
//                    binding.progressBar.visibility = View.VISIBLE
//                    viewModel.createNewOrder()
//                }else{
//                    binding.progressBar.visibility = View.GONE
//                    binding.klarnaLayout.visibility = View.GONE
//                    binding.mainLayout.visibility = View.VISIBLE
//                }
//
//
//            }
//
//            override fun onErrorOccurred(view: KlarnaPaymentView, error: KlarnaPaymentsSDKError) {
//                Log.e("meow", error.toString())
//                binding.progressBar.visibility =View.GONE
//                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onFinalized(
//                view: KlarnaPaymentView,
//                approved: Boolean,
//                authToken: String?
//            ) {
//
//
//            }
//
//            override fun onInitialized(view: KlarnaPaymentView) {
//
//                view.load(null)
//
//
//            }
//
//            override fun onLoadPaymentReview(view: KlarnaPaymentView, showForm: Boolean) {
//                println("LoadPaymentReview")
//
//            }
//
//            override fun onLoaded(view: KlarnaPaymentView) {
//                binding.klarnaLayout.visibility = View.VISIBLE
//                binding.mainLayout.visibility =View.GONE
//                binding.progressBar.visibility = View.GONE
//                println("Loaded")
//            }
//
//            override fun onReauthorized(
//                view: KlarnaPaymentView,
//                approved: Boolean,
//                authToken: String?
//            ) {
////                Toast.makeText(activity, "Reauthorized successfully", Toast.LENGTH_SHORT).show()
//            }
//        })


        binding.payButtonKlarna.setOnClickListener {
            paymentView.authorize(false, null)
//            paymentView.finalize(null)
//            paymentView.loadPaymentReview()
        }

//        paymentView.load(null)
//        paymentView.authorize(false, null)
    }


    private fun retrofit():KlarnaApi{
        return Retrofit.Builder()
            .baseUrl(KlarnaConst.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KlarnaApi::class.java)


    }

    override fun onAuthorized(
        view: KlarnaPaymentView,
        approved: Boolean,
        authToken: String?,
        finalizedRequired: Boolean?
    ) {
        if (approved){
            Toast.makeText(activity, "Approved", Toast.LENGTH_SHORT).show()
            CLIENT_TOKEN = null
            binding.progressBar.visibility = View.VISIBLE
            viewModel.createNewOrder()
        }else{
            binding.progressBar.visibility = View.GONE
            binding.klarnaLayout.visibility = View.GONE
            binding.mainLayout.visibility = View.VISIBLE
        }
    }

    override fun onErrorOccurred(view: KlarnaPaymentView, error: KlarnaPaymentsSDKError) {
        Log.e("meow", error.toString())
        binding.progressBar.visibility =View.GONE
        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
    }

    override fun onFinalized(view: KlarnaPaymentView, approved: Boolean, authToken: String?) {

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
        println("Loaded")
    }

    override fun onReauthorized(view: KlarnaPaymentView, approved: Boolean, authToken: String?) {

    }

}