package com.example.mealmoverskotlin.domain.viewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.data.models.OrderModel
import com.example.mealmoverskotlin.databinding.ActivityCartBinding
import com.example.mealmoverskotlin.domain.adapters.AdapterCartItems
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
import com.example.mealmoverskotlin.ui.restaurant_page.ConfirmOrderActivity
import java.text.DecimalFormat


class CartPageViewModel():ViewModel() {

    var order:OrderModel = DataHolder.order
    private lateinit var adapter:AdapterCartItems
    private lateinit var binding: ActivityCartBinding
    private lateinit var context: Context
    private val dc = DecimalFormat()

    fun init(context: Context, binding: ActivityCartBinding){
        try {
            this.context = context
            this.binding = binding
            dc.maximumFractionDigits = 2
            initPriceValues()
            initRecyclerView()
            onCheckOutButtonClick()
        }catch (e:Exception){
            Log.e("cartViewModel", e.toString())
        }


    }

    private fun onCheckOutButtonClick() {
        binding.checkoutButton.setOnClickListener {
            context.startActivity(Intent(context, ConfirmOrderActivity::class.java))
        }
    }


    private fun initPriceValues(){
        binding.totalPrice.text = "${((order.orderPrice + DataHolder.restaurant.deliveryPrice.toDouble())).trim1()}€"
        binding.deliveryFee.text = "${DataHolder.restaurant.deliveryPrice}€"
        binding.subTotal.text = (order.orderPrice).trim1()

    }
    fun onItemMinusClick(item:MenuItemModel){

        if (item.quantity == 1){
            order.items.remove(item)
            initRecyclerView()


        }else{
            item.quantity = item.quantity -1
        }


        if (order.items.isEmpty()){
            binding.mainLayout.visibility = View.GONE
            binding.emptyCartLayout.visibility = View.VISIBLE
        }
        reinitOrderPrice()

        initPriceValues()

    }

    private fun reinitOrderPrice() {
        order.orderPrice = 0.0
        order.itemsQuantity = 0
        order.items.onEach {
//            order.orderPrice = dc.format(order.orderPrice + (it.price.toFloat() * it.quantity)).toDouble()
            order.orderPrice = (order.orderPrice + (it.price.toDouble()*it.quantity)).trim1().toDouble()
            order.itemsQuantity = order.itemsQuantity + it.quantity
        }
    }

    fun onItemPlusClick(item:MenuItemModel){
        item.quantity = item.quantity + 1
        reinitOrderPrice()
        initPriceValues()
    }

    private fun initRecyclerView() {
        adapter = AdapterCartItems(context, order.items, this )
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
    }

}