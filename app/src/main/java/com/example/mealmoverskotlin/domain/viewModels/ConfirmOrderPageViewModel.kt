package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.databinding.ActivityConfirmOrderBinding
import com.example.mealmoverskotlin.domain.PayPal
import com.example.mealmoverskotlin.domain.dialogs.AddressFillingDialog
import com.example.mealmoverskotlin.domain.dialogs.DeliveryTimeDialog
import com.example.mealmoverskotlin.domain.dialogs.PaymentMethodDialog
import com.example.mealmoverskotlin.domain.firebase.FireStoreUseCase
import com.example.mealmoverskotlin.domain.klarna.KlarnaPayment
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
import com.example.mealmoverskotlin.domain.stripe.StripeUseCase
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.PaymentMethod
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
import com.example.mealmoverskotlin.ui.order.OrderCompletedActivity
import com.example.mealmoverskotlin.ui.restaurant_page.ConfirmOrderActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
@SuppressLint("StaticFieldLeak")

class ConfirmOrderPageViewModel @Inject constructor(
    private val repository: MainRepositoryInterface
) :ViewModel() {

    lateinit var binding: ActivityConfirmOrderBinding
    private lateinit var context: Context
    private lateinit var activity: ConfirmOrderActivity

    var paymentMethod: PaymentMethod = PaymentMethod.CASH
    lateinit var order:OrderModel
    var restaurant:RestaurantModel = DataHolder.restaurant
    var timeArray:MutableList<String> =  mutableListOf()
    var userAddress:AddressModel? = DataHolder.userAddress

    var loggedInUser:UserModel = DataHolder.loggedInUser!!
    private val firebase:FireStoreUseCase by lazy {
        FireStoreUseCase()
    }
    private lateinit var stripeUseCase: StripeUseCase
    private lateinit var lifecycleOwner: LifecycleOwner
    private val createOrderResponse: MutableLiveData<DataState<OrderModel>>  by lazy {
        MutableLiveData<DataState<OrderModel>>()
    }

    private val sendOrderToFirebaseResponse: MutableLiveData<DataState<Any?>>  by lazy {
        MutableLiveData<DataState<Any?>>()
    }

    private lateinit var deliveryTimeDialog:DeliveryTimeDialog
    private lateinit var addressDialog:AddressFillingDialog
    private lateinit var paymentMethodDialog: PaymentMethodDialog
    private val klarnaPayment: KlarnaPayment by lazy {
        KlarnaPayment(activity, binding, this)
    }
    private val payPal:PayPal by lazy {
        PayPal(activity)
    }

    fun init(binding: ActivityConfirmOrderBinding, activity: ConfirmOrderActivity){
        try {

            this.context = activity
            this.binding = binding
            this.lifecycleOwner = activity
            this.activity = activity
            stripeUseCase = StripeUseCase(activity)
            order = activity.intent.getSerializableExtra("order") as OrderModel
            addressDialog = AddressFillingDialog(context, this)
            paymentMethodDialog = PaymentMethodDialog(context, this)
            initPageValues()
            handleCardsLayoutClick()
            setTimeArray(11, 22)
        }catch (e:Exception){
            Log.e("confirmOrderViewModel", "m", e)
        }

    }

    private fun handleCardsLayoutClick() {
        binding.addressCard.setOnClickListener {
            addressDialog.dialog.show()
        }
        binding.paymentCard.setOnClickListener {
            paymentMethodDialog.dialog.show()
        }
        binding.timeCard.setOnClickListener {
            deliveryTimeDialog?.dialog?.show()

        }
    }

    fun initDialog(){
        deliveryTimeDialog = DeliveryTimeDialog(context, this)
    }

    fun onSelectDeliveryTime(time:String){
        order.deliveryTime = time
        binding.deliveryTimeTV.text = time

    }

    @SuppressLint("SetTextI18n")
    private fun initPageValues() {

        binding.totalPrice.text = (order.orderPrice + DataHolder.restaurant.deliveryPrice.toDouble()).trim1() + "€"
        binding.deliveryFee.text = restaurant.deliveryPrice + "€"
        binding.itemsTotal.text = (order.orderPrice).trim1() + "€"
        binding.deliveryTimeTV.text = order.deliveryTime
        binding.address = userAddress
        onPayButtonClick()

    }



    private fun onPayButtonClick(){
        binding.payButton.setOnClickListener {



            if (userAddress != null){
                when(""){
                    userAddress?.name ->  Toast.makeText(context, "Pleas enter customer name", Toast.LENGTH_SHORT).show()
                    else->{
                        when(paymentMethod){
                            PaymentMethod.CREDIT_CARD ->{

                                handleStripePayment()

                            }
                            PaymentMethod.CASH ->{

                                createNewOrder()
                            }
                            PaymentMethod.KLARNA -> {
                                binding.progressBar.visibility = View.VISIBLE
                                klarnaPayment.createPayment(order)
                            }
                            PaymentMethod.PAYPAL -> payPal.startPayment(order.orderPrice.toString())
                            else -> {}
                        }
                    }
                }
            }else{
                Toast.makeText(activity, "Please enter your address", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun handleStripePayment() {
        binding.progressBar.visibility =View.VISIBLE
        stripeUseCase.amount = (order.orderPrice + restaurant.deliveryPrice.toDouble()).trim1().replace(".", "")
        println(stripeUseCase.amount)
        viewModelScope.launch {
            stripeUseCase.createNewPayment()
        }
        stripeUseCase.paymentSheetLoading.observe(lifecycleOwner, Observer {
            if(!it){
                binding.progressBar.visibility =View.GONE
            }
        })

        stripeUseCase.paymentResult.observe(lifecycleOwner, Observer {
            if (it == "PAID"){
                createNewOrder()
                repository.updateLoggedInUser(activity.getSharedPreferences("PROFILE",Context.MODE_PRIVATE), DataHolder.loggedInUser!!)
            }else if (it == "CANCEL"){

            }
        })
    }

    fun createNewOrder(){
        order.address = userAddress!!
        order.created_at = LocalDateTime.now().toString()
        order.status = "new"
        order.restaurantName = restaurant.name
        order.userId = loggedInUser._id!!
       viewModelScope.launch {
           repository.createNewOrder(order).onEach {
                createOrderResponse.value = it
           }.launchIn(viewModelScope)


           createOrderResponse.observe(lifecycleOwner, Observer {
               when(it){
                   is DataState.Success ->{
//                       binding.progressBar.visibility = View.GONE
                       order._id = it.data._id
                       sendToFireBase()

                   }
                   is DataState.Error ->{
                       binding.progressBar.visibility = View.GONE
                       Toast.makeText(context, "Couldn't complete order please try again later", Toast.LENGTH_SHORT).show()
                   }

                   is DataState.Loading -> {
                       binding.progressBar.visibility = View.VISIBLE
                   }
               }
           })

       }

   }

    private fun sendToFireBase() {

        viewModelScope.launch {
            firebase.sendNewOrder(order).onEach {
                sendOrderToFirebaseResponse.value = it
            }.launchIn(viewModelScope)


            sendOrderToFirebaseResponse.observe(lifecycleOwner, Observer {
                when(it){
                    is DataState.Success ->{
                        binding.progressBar.visibility = View.GONE
                        DataHolder.reinitOrderValues()
                        val intent = Intent(context, OrderCompletedActivity::class.java)
                        intent.putExtra( "order_id",order._id)
                        intent.putExtra( "restaurantId",DataHolder.restaurant._id)
                        context.startActivity(intent)
                        activity.finish()
                        repository.updateUserAddress(activity.getSharedPreferences("PROFILE",Context.MODE_PRIVATE ),userAddress!!)
                    }
                    is DataState.Error ->{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Couldn't complete order please try again later", Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Loading->{
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            })



        }
    }

    private fun setTimeArray(min:Int, max:Int){

        timeArray.removeAll(timeArray)
        timeArray.add(0, "As soon as possible")

        (min..max).onEach { hour ->
            (0..60).onEach { minute->
                if (minute == 15 || minute == 30 || minute == 45 ) {
                    timeArray.add("$hour:$minute")
                }

                if (minute == 0){
                    timeArray.add("$hour:00")
                }

            }
        }

    }
}