package com.example.mealmoverskotlin.ui.restaurant_page

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.MenuItemModel
import com.example.mealmoverskotlin.databinding.FragmentCartBinding
import com.example.mealmoverskotlin.domain.CartItemClicksInterface
import com.example.mealmoverskotlin.domain.adapters.AdapterCartItems
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCheckoutVM
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
@SuppressLint("SetTextI18n")
class CartFragment : Fragment(), CartItemClicksInterface {

    private lateinit var binding:FragmentCartBinding
    private lateinit var viewModel:RestaurantAndCheckoutVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false)
        viewModel = ViewModelProvider(requireActivity())[RestaurantAndCheckoutVM::class.java]
        onArrowBackClick()
        initRecyclerView()
        initPriceValues()
        onCheckOutButtonClick()
        return binding.root
    }

    private fun onCheckOutButtonClick() {
        binding.checkoutButton.setOnClickListener {
            val intent = Intent(context, ConfirmOrderActivity::class.java)
            intent.putExtra("order", viewModel.order)
            requireContext().startActivity(intent)
        }
    }



    private fun initPriceValues(){
        binding.totalPrice.text = "${((viewModel.order.orderPrice + DataHolder.restaurant.deliveryPrice.toDouble())).trim1()}€"
        binding.deliveryFee.text = "${DataHolder.restaurant.deliveryPrice}€"
        binding.subTotal.text = (viewModel.order.orderPrice).trim1()

    }
     

    private fun reinitOrderPrice() {
        viewModel.order.orderPrice = 0.0
        viewModel.order.itemsQuantity = 0
        viewModel.order.items.onEach {
//            order.orderPrice = dc.format(order.orderPrice + (it.price.toFloat() * it.quantity)).toDouble()
            viewModel.order.orderPrice = (viewModel.order.orderPrice + (it.price.toDouble()*it.quantity)).trim1().toDouble()
            viewModel.order.itemsQuantity = viewModel.order.itemsQuantity + it.quantity
        }
    }


    private fun initRecyclerView() {
        val adapter = AdapterCartItems(requireContext(), viewModel.order.items , this)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
    }

    private fun onArrowBackClick() {
        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }



    override fun onPlusClick(item: MenuItemModel) {
        item.quantity = item.quantity + 1
        reinitOrderPrice()
        initPriceValues()
    }

    override fun onMinusClick(item: MenuItemModel) {
        if (item.quantity == 1){
            viewModel.order.items.remove(item)
            initRecyclerView()


        }else{
            item.quantity = item.quantity -1
        }


        if (viewModel.order.items.isEmpty()){
            binding.mainLayout.visibility = View.GONE
            binding.emptyCartLayout.visibility = View.VISIBLE
        }
        reinitOrderPrice()

        initPriceValues()
    }


}