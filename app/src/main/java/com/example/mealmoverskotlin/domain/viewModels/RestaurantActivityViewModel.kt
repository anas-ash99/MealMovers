package com.example.mealmoverskotlin.domain.viewModels

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.LifecycleOwner
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
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
import com.example.mealmoverskotlin.ui.restaurant_page.CartActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    override fun onPlusClick() {

    }

    override fun onAddToCartClick(item: MenuItemModel) {

    }


//    fun init(res:RestaurantModel, context: Context, lifecycleOwner: LifecycleOwner, binding: ActivityRestaurantBinding){
//
//        this.binding = binding
//        this.context = context
//        this.lifecycleOwner = lifecycleOwner
//        restaurant = res
//        binding.restaurant = restaurant
//        Glide.with(context).load(restaurant.image_url).into(binding.resImage)
//        initRecyclerView()
//
//        dialog = MenuItemDialog(context, this)
//        onCheckOutButtonClick()
//
////        println(restaurant?.checkIfOpen())
////        val opensAt = LocalDateTime.parse(restaurant.opensAt)
////        val closesAt = LocalDateTime.parse("restaurant.closesAt)
//
//        if (15 > restaurant.opensAt.substring(0,2).toInt() && LocalDateTime.now().hour.toDouble() < restaurant.opensAt.substring(0,2).toInt() ){
//            println(true)
//        }else{
//            println(LocalDateTime.now().hour)
//            println(restaurant.opensAt.substring(0,2).toInt())
//        }
//
//        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//
//        val day = "${LocalDateTime.now().year}-0${LocalDateTime.now().monthValue}-${LocalDateTime.now().dayOfMonth}"
//        val opensAt =  LocalDateTime.parse("$day ${restaurant.opensAt}", pattern)
//        val closesAt =  LocalDateTime.parse("$day ${restaurant.closesAt}", pattern)
//        println(opensAt)
//        println(closesAt)
//
//        println(closesAt.isAfter(LocalDateTime.now()) && !opensAt.isAfter(LocalDateTime.now()))
//
//    }
//
//
//    fun RestaurantModel.checkIfOpen():Boolean{
//        val opensAt = LocalDateTime.parse(this.opensAt)
//        val closesAt = LocalDateTime.parse(this.closesAt)
//
//
//        if (LocalDateTime.now().isAfter(opensAt) && !LocalDateTime.now().isAfter(closesAt)){
//            return true
//        }
//        return false
//
//    }
//    fun onStart(){
//
//        if (order.restaurant_id == restaurant._id && order.items.isNotEmpty()){
//            showCheckOutButton()
//        }else{
//            initOrder()
//            binding.checkoutButton.visibility =View.GONE
//        }
//    }
//
//    private fun initOrder(){
//        DataHolder.reinitOrderValues()
//        order =DataHolder.order
//        order.customerId = DataHolder.loggedInUser?._id!!
//        order.restaurant_id = restaurant._id
//        order.items.removeAll(order.items)
//    }
//
//    fun onItemClick(item:MenuItemModel){
//
//        dialog.item.quantity = item.quantity
//        dialog.item.price = item.price
//        dialog.item.imageUrl = item.imageUrl
//        dialog.item._id = item._id
//        dialog.item.name = item.name
//        dialog.item.description = item.description
//        dialog.showDialog()
//    }
//
//    private fun initRecyclerView() {
//        adapter = AdapterMenuItems(context, restaurant.menu_items, this)
//        binding.recyclerview.adapter = adapter
//        binding.recyclerview.layoutManager = LinearLayoutManager(context)
//    }
//
//    override fun onPlusClick() {
//
//    }
//
//    override fun onAddToCartClick(item: MenuItemModel) {
//
//        order.itemsQuantity = order.itemsQuantity + item.quantity
//        order.orderPrice = (item.price.toDouble() * item.quantity).trim1().toDouble() + order.orderPrice
//        var isItemExist = false
//
//
//        if(order.items.isNotEmpty()){
//            order.items.onEach {
//                if (it._id == item._id){
//                    isItemExist = true
//                    it.quantity = it.quantity  + item.quantity
//
//                }
//
//            }
//
//        }
//
//        if (!isItemExist){
//            order.items.add(MenuItemModel(_id = item._id, price = item.price, name = item.name, imageUrl = item.imageUrl, quantity = item.quantity))
//        }
//        showCheckOutButton()
//    }
//
//    private fun showCheckOutButton(){
//        binding.cartQuantity.text = order.itemsQuantity.toString()
//        binding.cartPrice.text =(order.orderPrice).trim1()
//        binding.checkoutButton.visibility = View.VISIBLE
//    }
//
//    private fun onCheckOutButtonClick(){
//        binding.checkoutButton.setOnClickListener {
//            context.startActivity(Intent(context, CartActivity::class.java))
//
//        }
//
//    }


}