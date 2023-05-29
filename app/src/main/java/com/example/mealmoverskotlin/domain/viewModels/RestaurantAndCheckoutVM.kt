package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.data.events.AddRestaurantToFavouritesEvent
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.shared.MenuItemsDialogInterface
import com.example.mealmoverskotlin.ui.dialogs.MenuItemDialog
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.UserRepository
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
import com.example.mealmoverskotlin.ui.restaurant_page.RestaurantActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class RestaurantAndCheckoutVM @Inject constructor (
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val userRepo:UserRepository
) : ViewModel(), MenuItemsDialogInterface {

    lateinit var restaurant: RestaurantModel
    lateinit var activity: RestaurantActivity
    val loggedInUser = DataHolder.loggedInUser
    private val dialog: MenuItemDialog by lazy {
        MenuItemDialog(activity, this)
    }
    val hasOrderChange by lazy {
        MutableLiveData<Boolean>()
    }
    var order = OrderModel()

    val menuSearchInput by lazy {
        MutableLiveData("")
    }
    val itemsSearchFor by lazy {
        MutableLiveData<List<MenuItemModel>>()
    }

    val addRestaurantToFavouritesEvent by lazy {
        MutableLiveData<AddRestaurantToFavouritesEvent<UserModel>>()
    }

    fun init(activity: RestaurantActivity){
        this.activity = activity
        getRestaurant()
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
       restaurant =  activity.intent.getSerializableExtra("RESTAURANT") as RestaurantModel
        order.restaurantName  = restaurant.name
        order.restaurant_id = restaurant._id
//        order.address.name = loggedInUser?.fullName!!
        order.userId = loggedInUser?._id!!
        DataHolder.userAddress?.let { order.address = it}
    }

    fun onItemClick(item: MenuItemModel) {
        dialog.item.quantity = item.quantity
        dialog.item.price = item.price
        dialog.item.imageUrl = item.imageUrl
        dialog.item._id = item._id
        dialog.item.name = item.name
        dialog.item.description = item.description
        dialog.showDialog()
    }

    override fun onPlusClick() {

    }

    override fun onAddToCartClick(item: MenuItemModel) {
        order.itemsQuantity = order.itemsQuantity + item.quantity
        order.orderPrice = (item.price.toDouble() * item.quantity).trim1().toDouble() + order.orderPrice
        var isItemExist = false

        if(order.items.isNotEmpty()){
            order.items.onEach {
                if (it._id == item._id){
                    isItemExist = true
                    it.quantity = it.quantity  + item.quantity

                }

            }

        }

        if (!isItemExist){
            order.items.add(MenuItemModel(_id = item._id, price = item.price, name = item.name, imageUrl = item.imageUrl, quantity = item.quantity))
        }

       hasOrderChange.value = true
        showCheckOutButton()

        ///////// or i can check if the order contains by simply using
        ////////  order.items.any { it._id == item._id })
    }

    private fun showCheckOutButton(){
//        binding.cartQuantity.text = order.itemsQuantity.toString()
//        binding.cartPrice.text =(order.orderPrice).trim1()
//        binding.checkoutButton.visibility = View.VISIBLE
    }

    fun updateLoggedInUser(){
        viewModelScope.launch {
            sharedPreferencesRepository.updateLoggedInUser(DataHolder.loggedInUser!!)
        }
    }

}