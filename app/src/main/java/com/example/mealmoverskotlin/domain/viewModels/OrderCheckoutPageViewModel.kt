package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.*
import com.example.mealmoverskotlin.databinding.ActivityConfirmOrderBinding
import com.example.mealmoverskotlin.domain.payments.PayPal
import com.example.mealmoverskotlin.ui.dialogs.AddressFillingDialog
import com.example.mealmoverskotlin.ui.dialogs.DeliveryTimeDialog
import com.example.mealmoverskotlin.ui.dialogs.PaymentMethodDialog
import com.example.mealmoverskotlin.domain.payments.klarna.KlarnaPayment
import com.example.mealmoverskotlin.domain.repositorylnterfaces.OrderRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.domain.repositoryImpl.StripeRepoImpl
import com.example.mealmoverskotlin.domain.repositorylnterfaces.KlarnaRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.StripeRepository
import com.example.mealmoverskotlin.domain.usecases.CheckIfRestaurantOpen
import com.example.mealmoverskotlin.domain.usecases.SetScheduleTimeArray
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

class OrderCheckoutPageViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val orderRepo:OrderRepository,
    private val setScheduleTimeArray: SetScheduleTimeArray,
    private val checkIfRestaurantOpen: CheckIfRestaurantOpen,
    private val klarnaRepository: KlarnaRepository,
    private val stripeRepository: StripeRepository
) :ViewModel() {

    lateinit var binding: ActivityConfirmOrderBinding
    private lateinit var context: Context
    private lateinit var activity: ConfirmOrderActivity
    var klarnaClintId = ""
    var paymentMethod = PaymentMethod.CASH
    lateinit var order:OrderModel
    var restaurant:RestaurantModel = DataHolder.restaurant
    val timeArray by lazy {
        setScheduleTimeArray.invoke(restaurant.hours, checkIfRestaurantOpen.invoke(restaurant.hours))
    }
    var userAddress:AddressModel? = DataHolder.userAddress
    var loggedInUser:UserModel = DataHolder.loggedInUser!!

    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var deliveryTimeDialog: DeliveryTimeDialog
    private lateinit var addressDialog: AddressFillingDialog
    private lateinit var paymentMethodDialog: PaymentMethodDialog


    val payPal: PayPal by lazy {
        PayPal(activity)
    }
    val _klarnaCreatePayment by lazy {   MutableLiveData<DataState<String>>()}
    val _stripeCreatePayment by lazy {   MutableLiveData<DataState<StripeResponse>>()}
    val _createNewOrder by lazy {   MutableLiveData<DataState<OrderModel>>()}

    fun init(binding: ActivityConfirmOrderBinding, activity: ConfirmOrderActivity){
        try {

            this.context = activity
            this.binding = binding
            this.lifecycleOwner = activity
            this.activity = activity

            order = activity.intent.getSerializableExtra("order") as OrderModel
            addressDialog = AddressFillingDialog(context, this)
            paymentMethodDialog = PaymentMethodDialog(context, this)
            initPageValues()
            handleCardsLayoutClick()

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
        binding.deliveryTimeTV.text = "Select time"


    }





    fun createKlarnaPayment() {
        if (klarnaClintId == ""){
            viewModelScope.launch {
                klarnaRepository.createPayment(order).onEach {
                    _klarnaCreatePayment.value = it
                }.launchIn(viewModelScope)
            }
        }else{
            _klarnaCreatePayment.value = DataState.Success(klarnaClintId)
        }
    }



    fun validatedUserAddress(): Boolean{
        var isValid = false
        userAddress?.let {
            isValid = true
            when(""){
                it.name -> isValid = false
                it.streetName -> isValid = false
                it.phoneNumber -> isValid = false
            }
        }

        return isValid
    }
    fun createStripePayment() {

        val amount = (order.orderPrice + restaurant.deliveryPrice.toDouble()).trim1().replace(".", "")
        viewModelScope.launch {
           stripeRepository.createPayment(amount).onEach {
               _stripeCreatePayment.value = it
           }.launchIn(viewModelScope)
        }
    }

    fun createNewOrder(){
        order.address = userAddress!!
        order.created_at = LocalDateTime.now().toString()
        order.status = "new"
        if (paymentMethod == PaymentMethod.CASH){
            order.paymentStatus ="NOT PAID"
        }else{
            order.paymentStatus = "PAID"
        }
        order.restaurantName = restaurant.name
        order.userId = loggedInUser._id!!


        viewModelScope.launch {

            orderRepo.createNewOrder2(order).onEach {
                _createNewOrder.value = it
            }.launchIn(viewModelScope)


        }


   }
    fun updateUserAddress(){
        viewModelScope.launch {
            sharedPreferencesRepository.updateLoggedInUser(DataHolder.loggedInUser!!)
            sharedPreferencesRepository.updateUserAddress(userAddress!!)
        }
    }

    fun updateLoggedInUser(){
        viewModelScope.launch {
            sharedPreferencesRepository.updateLoggedInUser(DataHolder.loggedInUser!!)
        }
    }

}