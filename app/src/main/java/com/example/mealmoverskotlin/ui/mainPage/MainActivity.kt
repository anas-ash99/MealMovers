package com.example.mealmoverskotlin.ui.mainPage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.databinding.ActivityMainBinding
import com.example.mealmoverskotlin.domain.adapters.AdapterRestaurantItem
import com.example.mealmoverskotlin.domain.adapters.AdapterCategoriesMain
import com.example.mealmoverskotlin.domain.dialogs.RestaurantsFilterDialog
import com.example.mealmoverskotlin.domain.viewModels.MainPageViewModel
import com.example.mealmoverskotlin.shared.Categories
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.ui.address.AddressActivity
import com.example.mealmoverskotlin.ui.authentication.AuthenticationActivity
import com.example.mealmoverskotlin.ui.order.OrdersHistoryActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel:MainPageViewModel by viewModels()
    private val filterDialog: RestaurantsFilterDialog by lazy {
        RestaurantsFilterDialog(this@MainActivity, viewModel)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.getLoggedInUser()
        observeLoggedInUser()
       handleBottomNavBarClicks()


    }

    private fun handleBottomNavBarClicks() {
        binding.bottomNavbar1.filterIcon.setOnClickListener {
            filterDialog.showDialog()
        }
    }

    private fun onAddressTextClick() {
        binding.topNavbar.addressHeader.setOnClickListener{
            startActivity(Intent(this, AddressActivity::class.java))
        }
    }
    private fun observeLoggedInUser(){
         viewModel.loggedInUser.observe(this){
             if (it != null){
                 initFunctions()
                 viewModel.getRestaurants()
             }else{
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
             }

         }
    }



    private fun observeIsFilterApplied(){
        viewModel.isFilterApplied.observe(this){
            if (it){
                binding.headerTitle.text = "Restaurants"
                binding.categoriesRecyclerView.visibility = View.GONE
            }else{
                binding.headerTitle.text = "All restaurants"
                binding.categoriesRecyclerView.visibility = View.VISIBLE
            }
        }

    }
    private fun initFunctions(){
        observeData()
        onErrorTryAgainClick()
        observeFilteredRestaurants()
        onMenuClick()
        handleNavigationDrawerClicks()
        onAddressTextClick()
        observeIsFilterApplied()
    }

    private fun observeData(){
        viewModel._restaurants.observe(this){
            when(it){
                is DataState.Success ->{
                    viewModel.allRestaurants.value = it.data
                   initCategoriesRecyclerView()
                   initRestaurantsItemRecyclerView(it.data)

                    binding.loading = false
                    binding.error = false
                }

                is DataState.Loading -> {
                    binding.error = false
                    binding.loading = true
                }

                is DataState.Error -> {
                    binding.error = true
                    binding.loading = false
                }
            }
        }
    }

    private fun onCategoryClick(category: Categories){

        if (category == Categories.ALL){
            initRestaurantsItemRecyclerView(viewModel.allRestaurants.value!!)
        }else{

            viewModel.filterRestaurants1(category.value.lowercase())
        }
    }


    private fun observeFilteredRestaurants(){
        viewModel.filteredRestaurants.observe(this){
            initRestaurantsItemRecyclerView(it)
        }
    }


    private fun initRestaurantsItemRecyclerView(list:List<RestaurantModel>){
        val adapter = AdapterRestaurantItem(this, list, ::onRestaurantClick )
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

    }

    private fun onRestaurantClick(res:RestaurantModel){

    }

    private fun onMenuClick() {
        binding.topNavbar.menuIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }
    }

    private fun onErrorTryAgainClick() {
        binding.tamplateNetwordError.tryAgainButton.setOnClickListener {
            viewModel.getRestaurants()
        }
    }
    override fun onStart() {
        super.onStart()
        if (DataHolder.userAddress == null){
            binding.topNavbar.addressHeaderTV.text = "Select your address"
        }else{
            binding.topNavbar.addressHeaderTV.text = "${DataHolder.userAddress?.streetName}  ${DataHolder.userAddress?.houseNumber}"

        }
    }
    private fun handleNavigationDrawerClicks(){
        binding.navigationDrawer.setNavigationItemSelectedListener { item ->
            when (item.title.toString()) {
                "Orders" -> {
                  startActivity(Intent(this, OrdersHistoryActivity::class.java))
                }
                "Sign out" -> {
                    viewModel.signOutUser()
                    startActivity(Intent(this, AuthenticationActivity::class.java))
                    finish()
                }
            }

            true
        }
    }


    private fun initCategoriesRecyclerView() {
        val categoriesAdapter = AdapterCategoriesMain(this, viewModel.currentCategory,:: onCategoryClick )
        binding.categoriesRecyclerView.adapter = categoriesAdapter
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.scrollToPosition(categoriesAdapter.items.indexOf(viewModel.currentCategory))
    }



}