package com.example.mealmoverskotlin.domain.viewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mealmoverskotlin.data.models.*
import com.example.mealmoverskotlin.databinding.ActivityRestaurantBinding
import com.example.mealmoverskotlin.domain.MenuItemsDialogInterface
import com.example.mealmoverskotlin.domain.adapters.AdapterMenuItems
import com.example.mealmoverskotlin.domain.dialogs.MenuItemDialog
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.PriceTrimmer
import com.example.mealmoverskotlin.ui.restaurant_page.CartActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DecimalFormat
import javax.inject.Inject


@HiltViewModel
class RestaurantActivityViewModel @Inject constructor (
    private val repository: MainRepositoryInterface
        ): ViewModel(), MenuItemsDialogInterface {
    lateinit var restaurant:RestaurantModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var binding: ActivityRestaurantBinding
    private lateinit var context:Context
    private lateinit var adapter:AdapterMenuItems
    private lateinit var dialog:MenuItemDialog
    private var order:OrderModel = DataHolder.order



    fun init(res:RestaurantModel, context: Context, lifecycleOwner: LifecycleOwner, binding: ActivityRestaurantBinding){
        try {
            this.binding = binding
            this.context = context
            this.lifecycleOwner = lifecycleOwner
            restaurant = res
            binding.restaurant = restaurant
            Glide.with(context).load(restaurant.image_url).into(binding.resImage)
            initRecyclerView()

            dialog = MenuItemDialog(context, this)
            onCheckOutButtonClick()
        }catch (e:Exception){
            Log.e("error", e.toString())
        }


    }


    fun getRestaurantFromApi(){



    }
    fun onStart(){

        if (order.restaurant_id == restaurant._id && order.items.isNotEmpty()){
            showCheckOutButton()
        }else{
//            order = OrderModel(restaurant_id = restaurant._id, customer = UserModel(fullName = "Anas Ashraf"), address = , deliveryTime = restaurant.deliveryTime )
            initOrder()
            binding.checkoutButton.visibility =View.GONE
        }
    }

    private fun initOrder(){
        DataHolder.reinitOrderValues()
        order =DataHolder.order
        order.customerId = DataHolder.loggedInUser?._id!!
        order.restaurant_id = restaurant._id
        order.items.removeAll(order.items)
//        order.address = AddressModel(city = "Bonn", zipCode = "53115", streetName = "Nidggerstr.", houseNumber = "12")
    }

    fun onItemClick(item:MenuItemModel){

        dialog.item.quantity = item.quantity
        dialog.item.price = item.price
        dialog.item.imageUrl = item.imageUrl
        dialog.item._id = item._id
        dialog.item.name = item.name
        dialog.item.description = item.description
//        item.quantity = 1
        dialog.showDialog()
    }

    private fun initRecyclerView() {
        adapter = AdapterMenuItems(context, restaurant.menu_items, this)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
    }

    override fun onPlusClick() {

    }

    override fun onAddToCartClick(item: MenuItemModel) {

        order.itemsQuantity = order.itemsQuantity + item.quantity
        order.orderPrice = PriceTrimmer.trim(item.price.toDouble() * item.quantity).toDouble() + order.orderPrice
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
//        if (!order.items.contains(item)){
//            order.items.add(MenuItemModel(_id = item._id, price = item.price, name = item.name, imageUrl = item.imageUrl, quantity = item.quantity))
//            println(order.items[0])
//        }else{
//
//            order.items.onEach {
//                if (it._id == item._id){
//                    isItemExist = true
//                    it.quantity = it.quantity  + item.quantity
//
//                }
//
//            }
////            item.quantity = item.quantity + order.items.get(order.items.indexOf(item)).quantity
////            order.items.remove(item)
////            order.items.add(item)
//        }
//        order.itemsQuantity = 0
//        order.orderPrice = 0.0
//        order.items.onEach {
//            order.itemsQuantity = order.itemsQuantity + it.quantity
//            order.orderPrice = PriceTrimmer.trim(order.orderPrice + it.quantity * it.price.toDouble()).toDouble()
//        }

//        order.items.add(item)
        showCheckOutButton()
    }

    private fun showCheckOutButton(){
        binding.cartQuantity.text = order.itemsQuantity.toString()
        binding.cartPrice.text = "${PriceTrimmer.trim(order.orderPrice)}€"
        binding.checkoutButton.visibility = View.VISIBLE
    }

    private fun onCheckOutButtonClick(){
        binding.checkoutButton.setOnClickListener {
            context.startActivity(Intent(context, CartActivity::class.java))

        }

    }


}