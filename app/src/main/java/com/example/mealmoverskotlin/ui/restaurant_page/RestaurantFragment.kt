package com.example.mealmoverskotlin.ui.restaurant_page

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.events.AddRestaurantToFavouritesEvent
import com.example.mealmoverskotlin.databinding.FragmentRestaurantBinding
import com.example.mealmoverskotlin.ui.adapters.AdapterMenuItems
import com.example.mealmoverskotlin.domain.viewModels.RestaurantAndCartVM
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.shared.extension_methods.PriceTrimmer.trim1
import com.example.mealmoverskotlin.ui.dialogs.RestaurantClosedDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@SuppressLint("SetTextI18n")

class RestaurantFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantBinding
    private lateinit var viewModel: RestaurantAndCartVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant, container, false)
        viewModel = ViewModelProvider(requireActivity())[RestaurantAndCartVM::class.java]

        initRecyclerView()
        initRestaurantValues()
        onArrowBackClick()
        onCheckOutButtonClick()
        observeHasOrderChange()
        onSearchBarClick()
        initFavouriteIcon()
        addResToFavourites()

        return binding.root
    }

    private fun addResToFavourites() {
        binding.heartCard.setOnClickListener {
            viewModel.addRestaurantToFavourites()

        }

        viewModel.addRestaurantToFavouritesEvent.observe(requireActivity()){
            when(it){
                is AddRestaurantToFavouritesEvent.Error -> Toast.makeText(requireContext(), "Couldn't add restaurant to favourites", Toast.LENGTH_SHORT).show()
                AddRestaurantToFavouritesEvent.Loading -> {}
                is AddRestaurantToFavouritesEvent.Success -> {
                    DataHolder.loggedInUser = it.data
                    viewModel.updateLoggedInUser()
                    initFavouriteIcon()

                }
            }
        }
    }

    private fun initFavouriteIcon(){
        if (DataHolder.loggedInUser?.favouriteRestaurants?.contains(viewModel.restaurant._id)!!){

            binding.heartIcon.setBackgroundResource(R.drawable.ic_baseline_favorite_24)

        }else{
            binding.heartIcon.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)

        }
    }

    private fun onSearchBarClick() {
        binding.searchItemBar.setOnClickListener {

            requireActivity().supportFragmentManager.commit {
                setCustomAnimations(R.anim.slide_in_up, R.anim.slide_down_immediatley)
                replace(R.id.fragment_layout, SearchMenuFragment(),"search_fragment")
                addToBackStack(null)
            }

        }
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