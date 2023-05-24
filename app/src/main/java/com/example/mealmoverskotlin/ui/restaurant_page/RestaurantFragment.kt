package com.example.mealmoverskotlin.ui.restaurant_page

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentResultOwner
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.FragmentRestaurantBinding
import com.example.mealmoverskotlin.domain.adapters.AdapterMenuItems
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCheckoutVM
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("SetTextI18n")
class RestaurantFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantBinding
    private lateinit var viewModel: RestaurantAndCheckoutVM


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant, container, false)
        viewModel = ViewModelProvider(requireActivity())[RestaurantAndCheckoutVM::class.java]
        initRecyclerView()
        initRestaurantValues()
        onArrowBackClick()
        onCheckOutButtonClick()
         observeHasOrderChange()

        return binding.root
    }

    private fun observeHasOrderChange() {
        viewModel.hasOrderChange.observe(requireActivity()){
            if (it){
                showCheckOutButton()
            }
        }
    }

    private fun showCheckOutButton(){
        binding.cartQuantity.text = viewModel.order.itemsQuantity.toString()
        binding.cartPrice.text ="${viewModel.order.orderPrice.trim1()}€"
        binding.checkoutButton.visibility = View.VISIBLE
    }
    private fun onCheckOutButtonClick() {
        binding.checkoutButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_layout, CartFragment(),"cart_fragment")
                addToBackStack("restaurant_fragment")
                commit()
            }
        }
    }

    private fun onArrowBackClick() {
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initRestaurantValues() {
        binding.restaurant = viewModel.restaurant
        if (viewModel.restaurant.image_url != ""){
            Glide.with(requireActivity()).load(viewModel.restaurant.image_url).into(binding.resImage)
        }
    }


    private fun initRecyclerView() {
        val adapter = AdapterMenuItems(requireContext(), viewModel.restaurant.menu_items, viewModel)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }


}