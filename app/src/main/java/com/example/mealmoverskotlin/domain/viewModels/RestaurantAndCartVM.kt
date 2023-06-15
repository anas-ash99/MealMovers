package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.data.events.AddRestaurantToFavouritesEvent
import com.example.mealmoverskotlin.data.events.CartEvent
import com.example.mealmoverskotlin.data.models.*
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.UserRepository
import com.example.mealmoverskotlin.domain.usecases.restaurantPageUseCases.CheckIfRestaurantOpen
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.priceTrim
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class RestaurantAndCartVM @Inject constructor (
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val userRepo:UserRepository,
    private val checkIfRestaurantOpen: CheckIfRestaurantOpen,
) : ViewModel() {

    lateinit var restaurant: RestaurantModel

    val loggedInUser = DataHolder.loggedInUser
    val cart by lazy {
        MutableLiveData<MutableList<CartItemModel>>(mutableListOf())
    }
   val isResOpen by lazy { MutableLiveData(true) }

    val cartEvent by lazy {
        MutableLiveData<CartEvent>()
    }

    val hasOrderChange by lazy {
        MutableLiveData<Boolean>()
    }
    var order = OrderModel()

    val itemsSearchFor by lazy {
        MutableLiveData<List<MenuItemModel>>()
    }
    val addRestaurantToFavouritesEvent by lazy {
        MutableLiveData<AddRestaurantToFavouritesEvent<UserModel>>()
    }

    fun init(){
        getRestaurant()
        isResOpen.value = checkIfRestaurantOpen.invoke(restaurant.hours)
    }

    fun addRestaurantToFavourites(){
        viewModelScope.launch {

            userRepo.addRestaurantToFavourites(DataHolder.loggedInUser?._id!!, restaurant._id).onEach {
                addRestaurantToFavouritesEvent.value = it
            }.launchIn(viewModelScope)

        }
    }
    fun handleSearchItems(input:String){
       itemsSearchFor.value =  restaurant.menu_items.filter{ it.name.lowercase().contains(input) }
    }

    private fun getRestaurant(){
        order.restaurantName  = restaurant.name
        order.restaurant_id = restaurant._id
        order.userId = loggedInUser?._id!!
        DataHolder.userAddress?.let { order.address = it}
    }



    fun addItemToCart(item: MenuItemModel, qut:Int){
       val cartItem =  cart.value?.filter { it.item._id == item._id }!!
        if (cartItem.isEmpty()){
            cart.value?.add(CartItemModel(item, qut))
        }else{
            cartItem[0].quantity += qut
        }
        order.itemsQuantity += qut
        order.orderPrice += (qut * item.price.toDouble()).priceTrim().toDouble()
        cart.value = cart.value
    }



    fun removeItemFromCart(item: MenuItemModel, qut:Int){
        val cartItem =  cart.value?.filter { it.item._id == item._id }!!
        if (cartItem.isNotEmpty()){
            if (cartItem[0].quantity == 1){
                cart.value?.remove(cartItem[0])
            }else{
                cartItem[0].quantity -= 1
            }
        }

        order.itemsQuantity -= qut
        order.orderPrice -= (qut * item.price.toDouble()).priceTrim().toDouble()
        cart.value = cart.value
    }



    fun updateLoggedInUser(){
        viewModelScope.launch {
            sharedPreferencesRepository.updateLoggedInUser(DataHolder.loggedInUser!!)
        }
    }

}