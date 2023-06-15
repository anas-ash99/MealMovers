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
import com.example.mealmoverskotlin.data.models.CartItemModel
import com.example.mealmoverskotlin.databinding.FragmentCartBinding
import com.example.mealmoverskotlin.ui.adapters.AdapterCartItems
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCartVM
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.priceTrim
@SuppressLint("SetTextI18n")
class CartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding
    private lateinit var viewModel:RestaurantAndCartVM
    private lateinit var adapter:AdapterCartItems
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false)
        viewModel = ViewModelProvider(requireActivity())[RestaurantAndCartVM::class.java]
        onArrowBackClick()
        initRecyclerView()
        initPriceValues()
        onCheckOutButtonClick()

        return binding.root
    }

    private fun onCheckOutButtonClick() {
        binding.checkoutButton.setOnClickListener {
            viewModel.order.cart = viewModel.cart.value!!
            val intent = Intent(context, ConfirmOrderActivity::class.java)
            intent.putExtra("order", viewModel.order)
            requireContext().startActivity(intent)
        }
    }



    private fun initPriceValues(){
        binding.totalPrice.text = "${((viewModel.order.orderPrice + viewModel.restaurant.deliveryPrice.toDouble())).priceTrim()}€"
        binding.deliveryFee.text = "${DataHolder.restaurant.deliveryPrice}€"
        binding.subTotal.text = "${(viewModel.order.orderPrice).priceTrim()}€"
    }
     


    private fun initRecyclerView() {
       adapter = AdapterCartItems(requireContext(), viewModel.cart.value!! , ::onPlusClick, ::onMinusClick)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
    }

    private fun onArrowBackClick() {
        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }



    private fun onPlusClick(item: CartItemModel) {

        viewModel.addItemToCart(item.item, 1)
        adapter.updateItem(viewModel.cart.value?.indexOf(item)!!)
        initPriceValues()
    }

    private fun onMinusClick(item: CartItemModel) {
        val index = viewModel.cart.value?.indexOf(item)!!
        val itemQut = item.quantity
        viewModel.removeItemFromCart(item.item,1)
        if (itemQut == 1){
            adapter.removeItem(index)

        }else{
            adapter.updateItem(index)
        }


        if (viewModel.cart.value?.isEmpty()!!){
            binding.mainLayout.visibility = View.GONE
            binding.emptyCartLayout.visibility = View.VISIBLE
        }

        initPriceValues()
    }


}