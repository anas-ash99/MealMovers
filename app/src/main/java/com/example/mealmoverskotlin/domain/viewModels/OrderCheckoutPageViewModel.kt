package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.*
import com.example.mealmoverskotlin.domain.repositorylnterfaces.OrderRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.KlarnaRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.StripeRepository
import com.example.mealmoverskotlin.domain.usecases.restaurantPageUseCases.CheckIfRestaurantOpen
import com.example.mealmoverskotlin.domain.usecases.confirnOrderPage.SetScheduleTimeArray
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.PaymentMethod
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
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

    private var klarnaClintId = ""
    val paymentMethod by lazy {
        MutableLiveData(PaymentMethod.CASH)
    }
    val timeArray by lazy {
        setScheduleTimeArray.invoke(restaurant.hours, checkIfRestaurantOpen.invoke(restaurant.hours))
    }
    var order:OrderModel = OrderModel()

    val deliveryTime by lazy {
        MutableLiveData(timeArray[0])
    }
    var restaurant:RestaurantModel = DataHolder.restaurant
    val userAddress by lazy{ MutableLiveData(DataHolder.userAddress)}
    var loggedInUser:UserModel = DataHolder.loggedInUser!!




    val _klarnaCreatePayment by lazy {   MutableLiveData<DataState<String>>()}
    val _stripeCreatePayment by lazy {   MutableLiveData<DataState<StripeResponse>>()}
    val _createNewOrder by lazy {   MutableLiveData<DataState<OrderModel>>()}






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
        userAddress.value?.let {
            isValid = true
            when(""){
                it.name -> isValid = false
                it.streetName -> isValid = false
//                it.phoneNumber -> isValid = false
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
        order.address = userAddress.value!!
        order.created_at = LocalDateTime.now().toString()
        order.status = "new"
        if (paymentMethod.value == PaymentMethod.CASH){
            order.paymentStatus ="NOT PAID"
        }else{
            order.paymentStatus = "PAID"
        }
        order.restaurantName = restaurant.name
        order.userId = loggedInUser._id!!


        viewModelScope.launch {

            orderRepo.createNewOrder(order).onEach {
                _createNewOrder.value = it
            }.launchIn(viewModelScope)


        }


   }
    fun updateUserAddress(){
        viewModelScope.launch {
            sharedPreferencesRepository.updateLoggedInUser(DataHolder.loggedInUser!!)
            sharedPreferencesRepository.updateUserAddress(userAddress.value!!)
        }
    }

    fun updateLoggedInUser(){
        viewModelScope.launch {
            sharedPreferencesRepository.updateLoggedInUser(DataHolder.loggedInUser!!)
        }
    }

}