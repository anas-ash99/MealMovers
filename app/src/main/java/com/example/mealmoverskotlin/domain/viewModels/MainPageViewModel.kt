package com.example.mealmoverskotlin.domain.viewModels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.databinding.ActivityMainBinding
import com.example.mealmoverskotlin.domain.LastSeenLocation

import com.example.mealmoverskotlin.domain.adapters.AdapterRestaurantItem
import com.example.mealmoverskotlin.domain.adapters.Adapter_categories_main
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.network_connection.NetworkConnection
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
import com.example.mealmoverskotlin.shared.Categories
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.ui.address.AddressActivity
import com.example.mealmoverskotlin.ui.authentication.AuthenticationActivity
import com.example.mealmoverskotlin.ui.mainPage.MainActivity
import com.example.mealmoverskotlin.ui.order.OrderActivity
import com.example.mealmoverskotlin.ui.order.OrdersHistoryActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val repo: MainRepositoryInterface,

):ViewModel() {
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var binding:ActivityMainBinding
    private var isLoadingDone = false
    private var currentCategory:Categories = Categories.ALL
    private lateinit var adapter: AdapterRestaurantItem
    private lateinit var categoriesAdapter: Adapter_categories_main
    private var loggedInUser:UserModel? = null
    var userAddress:AddressModel? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var activity:Activity
    private var isLocationGranted:Boolean = false
    private lateinit var networkConnection: NetworkConnection
    private lateinit var lastSeenLocation: LastSeenLocation
   val allRestaurantsResponse:MutableLiveData<DataState<MutableList<RestaurantModel>>> by lazy {
       MutableLiveData<DataState<MutableList<RestaurantModel>>>()
   }
    val allRestaurants: MutableLiveData<MutableList<RestaurantModel>> by lazy {
        MutableLiveData<MutableList<RestaurantModel>>()
    }
    val sushiRestaurants: MutableLiveData<MutableList<RestaurantModel>> by lazy {
        MutableLiveData<MutableList<RestaurantModel>>()
    }
    val pizzaRestaurants: MutableLiveData<MutableList<RestaurantModel>> by lazy {
        MutableLiveData<MutableList<RestaurantModel>>()
    }
    val burgerRestaurants: MutableLiveData<MutableList<RestaurantModel>> by lazy {
        MutableLiveData<MutableList<RestaurantModel>>()
    }
    val donerRestaurants: MutableLiveData<MutableList<RestaurantModel>> by lazy {
        MutableLiveData<MutableList<RestaurantModel>>()
    }




  fun initPage(lifecycleOwner: LifecycleOwner, activity:Activity, binding: ActivityMainBinding, sharedPreferences: SharedPreferences ){
      this.binding = binding
      this.lifecycleOwner = lifecycleOwner
      this.sharedPreferences = sharedPreferences
      this.activity = activity
      networkConnection = NetworkConnection(activity)
      getLoggedInUser()
      if (loggedInUser == null) {
          activity.startActivity(Intent(activity, AuthenticationActivity::class.java))
          activity.finish()
      }else{
          getRestaurants()
      }
      onTryAgainErrorClick()

  }

    private fun onAddressTextClick() {
        binding.topNavbar.addressHeader.setOnClickListener{
            activity.startActivity(Intent(activity, AddressActivity::class.java))
        }
    }

    fun handleCategoryClick(item:Categories){
        currentCategory = item
        when(item){
            Categories.ALL-> initRestaurantsItemRecyclerView(allRestaurants.value!!)
            Categories.PIZZA -> initRestaurantsItemRecyclerView(pizzaRestaurants.value!!)
            Categories.SUSHI -> initRestaurantsItemRecyclerView(sushiRestaurants.value!!)
            Categories.DONER -> initRestaurantsItemRecyclerView(donerRestaurants.value!!)
            Categories.BURGER -> initRestaurantsItemRecyclerView(burgerRestaurants.value!!)


        }
    }


    private fun handleNavigationDrawerClicks(){
        binding.navigationDrawer.setNavigationItemSelectedListener { item ->
            when (item.title.toString()) {
                "Orders" -> {
                   activity.startActivity(Intent(activity, OrdersHistoryActivity::class.java))
                }
                "Sign out" -> {

                    repo.deleteLoggedInUser(sharedPreferences)
                    repo.deleteUserAddress(sharedPreferences)
                    DataHolder.userAddress = null
                    DataHolder.userAddress = null
                    activity.startActivity(Intent(activity, AuthenticationActivity::class.java))
                    activity.finish()
                }
            }

            true
        }
    }

    private fun initRestaurantsItemRecyclerView(list:MutableList<RestaurantModel>){
        adapter = AdapterRestaurantItem(activity, list )
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(activity!!)
        binding.loadingLayout.visibility = View.GONE
        binding.mainLayout1.visibility = View.VISIBLE

    }
    private fun initCategoriesRecyclerView() {
        categoriesAdapter = Adapter_categories_main(activity, this, currentCategory)
        binding.categoriesRecyclerView.adapter = categoriesAdapter
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.scrollToPosition(categoriesAdapter.items.indexOf(currentCategory))
    }
    private fun getRestaurants(){
        binding.mainLayout1.visibility = View.GONE
        binding.tamplateNetwordError.layout.visibility = View.GONE
        binding.loadingLayout.visibility =View.VISIBLE

        viewModelScope.launch {

            repo.getAllRestaurants2(object : OnDone {
                override fun onLoadingDone(res: Any?) {
                    allRestaurants.value = res as MutableList<RestaurantModel>
                    initRestaurantsItemRecyclerView(allRestaurants.value!!)
                    initRestaurants()
                    isLoadingDone = true
                    handleNavigationDrawerClicks()
                    onAddressTextClick()
                    getUserAddress()
                    handleCategoryClick(currentCategory)
                    initCategoriesRecyclerView()
                    onMenuClick()
                    binding.tamplateNetwordError.layout.visibility = View.GONE

                }

                override fun onError(e: Exception) {
                    Thread.sleep(500)
                    binding?.loadingLayout?.visibility = View.GONE
                    binding.mainLayout1.visibility = View.GONE
                    binding.tamplateNetwordError.layout.visibility = View.VISIBLE


                }
            })
        }
//        viewModelScope.launch {
//            repo.getAllRestaurants().onEach {
//                allRestaurantsResponse.value = it
//            }.launchIn(viewModelScope)
//
//            allRestaurantsResponse.observe(lifecycleOwner!!, Observer {
//
//                when(it){
//                    is DataState.Success->{
//                        allRestaurants.value = it.data
//                         initRestaurantsItemRecyclerView(allRestaurants.value!!)
//                        initRestaurants()
//                        isLoadingDone = true
//                        handleNavigationDrawerClicks()
//                        onAddressTextClick()
//                        getUserAddress()
//                        handleCategoryClick(currentCategory)
//                        initCategoriesRecyclerView()
//                        onMenuClick()
//                        binding.tamplateNetwordError.layout.visibility = View.GONE
//                    }
//                    is DataState.Error->{
//
//                        binding?.loadingLayout?.visibility = View.GONE
//                        binding.mainLayout1.visibility = View.GONE
////                        binding?.errorTamplate?.visibility = View.VISIBLE
//                        binding.tamplateNetwordError.layout.visibility = View.VISIBLE
//                    }
//
//
//                    else -> {}
//                }
//            })
//
//
//
//
//
//        }
    }


    private fun onTryAgainErrorClick(){


        binding.tamplateNetwordError.tryAgainButton.setOnClickListener {
            getRestaurants()
        }

    }
    private fun initRestaurants() {
        sushiRestaurants.value = allRestaurants.value?.filter {
            it.categories.contains("sushi")
        } as MutableList<RestaurantModel>?
        donerRestaurants.value = allRestaurants.value?.filter {
            it.categories.contains("döner")
        } as MutableList<RestaurantModel>?
        pizzaRestaurants.value = allRestaurants.value?.filter {
            it.categories.contains("pizza")
        } as MutableList<RestaurantModel>?
        burgerRestaurants.value = allRestaurants.value?.filter {
            it.categories.contains("burger")
        } as MutableList<RestaurantModel>?

    }
    private fun onMenuClick() {
        binding?.topNavbar?.menuIcon?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(Gravity.LEFT)
        }
    }



    private fun getLoggedInUser(){
        DataHolder.loggedInUser = repo.getLoggedInUser(sharedPreferences)
        loggedInUser = DataHolder.loggedInUser


    }

    fun updateLoggedInUser(user:UserModel){
        repo.updateLoggedInUser(sharedPreferences,user)
    }


    private fun getUserAddress(){

        DataHolder.userAddress = repo.getUserAddress(sharedPreferences)
        userAddress = DataHolder.userAddress
        binding.topNavbar.addressHeader.text = "${userAddress?.streetName} ${userAddress?.houseNumber}"
    }






}