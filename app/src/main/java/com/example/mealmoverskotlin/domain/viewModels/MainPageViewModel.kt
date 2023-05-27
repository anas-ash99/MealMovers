package com.example.mealmoverskotlin.domain.viewModels



import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.view.Gravity
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.databinding.ActivityMainBinding
import com.example.mealmoverskotlin.domain.adapters.AdapterRestaurantItem
import com.example.mealmoverskotlin.domain.adapters.AdapterCategoriesMain
import com.example.mealmoverskotlin.domain.dialogs.RestaurantsFilterDialog
import com.example.mealmoverskotlin.domain.network_connection.NetworkConnection
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.shared.Categories
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.ui.address.AddressActivity
import com.example.mealmoverskotlin.ui.authentication.AuthenticationActivity
import com.example.mealmoverskotlin.ui.mainPage.MainActivity
import com.example.mealmoverskotlin.ui.order.OrdersHistoryActivity
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val repo: MainRepositoryInterface,
    private val sharedPreferencesRepository: SharedPreferencesRepository

):ViewModel() {

    var currentCategory:Categories = Categories.ALL


    private lateinit var sharedPreferences: SharedPreferences
    var hasSelectedFilterItemsChanged:Boolean = false
    var hasSelectedSortItemChanged:Boolean = false

    val _restaurants:MutableLiveData<DataState<List<RestaurantModel>>> by lazy {
        MutableLiveData<DataState<List<RestaurantModel>>>()
    }
    val filteredRestaurants: MutableLiveData<List<RestaurantModel>> by lazy {
        MutableLiveData<List<RestaurantModel>>()
    }
    val loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val allRestaurants: MutableLiveData<List<RestaurantModel>> by lazy {
        MutableLiveData<List<RestaurantModel>>()
    }

    val loggedInUser:MutableLiveData<UserModel> by lazy {
        MutableLiveData<UserModel>()
    }

    val userAddress:MutableLiveData<AddressModel> by lazy {
        MutableLiveData<AddressModel>()
    }





  fun initPage(){
//      this.binding = binding
//      this.sharedPreferences = sharedPreferences
//      this.activity = activity
//      filterDialog = RestaurantsFilterDialog(activity, this)
//      networkConnection = NetworkConnection(activity)
//      getLoggedInUser()
//
//
//      if (loggedInUser == null) {
////          activity.startActivity(Intent(activity, AuthenticationActivity::class.java))
////          activity.finish()
//      }else{
//
//      }


  }

    private fun initFunctions(){

//        initRestaurantsItemRecyclerView(allRestaurants.value!!)
//        filterRestaurants()
//        handleNavigationDrawerClicks()
//        onAddressTextClick()
//        getUserAddress()
//        handleCategoryClick(currentCategory)
//        initCategoriesRecyclerView()
//        onMenuClick()
//        filterButtonClick()

    }





    fun getRestaurants(){

        viewModelScope.launch {
            repo.getAllRestaurants().onEach {
               _restaurants.value = it

            }.launchIn(viewModelScope)

        }

    }



    fun signOutUser(){
        viewModelScope.launch {
            sharedPreferencesRepository.deleteUserAddress()
            sharedPreferencesRepository.deleteLoggedInUser()
        }

        DataHolder.loggedInUser = null
        DataHolder.userAddress = null
    }

    fun filterRestaurants1(category:String) {
        filteredRestaurants.value = allRestaurants.value?.filter {
            it.categories.contains(category)
        }

    }


   fun getLoggedInUser(){


            DataHolder.loggedInUser = sharedPreferencesRepository.getLoggedInUser()
            loggedInUser.value = DataHolder.loggedInUser



    }

    fun updateLoggedInUser(user:UserModel){
        repo.updateLoggedInUser(sharedPreferences,user)
    }



    fun getUserAddress(){

        viewModelScope.launch {
            DataHolder.userAddress = sharedPreferencesRepository.getUserAddress()
            userAddress.value = DataHolder.userAddress
        }
//        if (userAddress != null){
//            binding.topNavbar.addressHeaderTV.text = "${userAddress?.streetName} ${userAddress?.houseNumber}"
//
//        }
    }


    private fun filterButtonClick(){
//        binding.bottomNavbar1.filterIcon.setOnClickListener {
//
//            if (!hasSelectedFilterItemsChanged){
//                filterDialog.filterItems.value?.removeAll(filterDialog.filterItems.value!!)
//                filterDialog.filterItems.value = filterDialog.filterItems.value
//            }
//
//            if (!hasSelectedSortItemChanged){
//                filterDialog.sortItem.value = "Recommended"
//            }
//            filterDialog.dialog.show()
//        }
    }


    fun onDialogApplyClick(filterItems: MutableList<String>, sortType:String){

        hasSelectedFilterItemsChanged = filterItems.isNotEmpty()!!
        hasSelectedSortItemChanged = sortType != "Recommended"
        println(filterItems)
        var filteredList = mutableListOf<RestaurantModel>()
        if (filterItems.isEmpty()!!){
//            binding.headerTitle.text = "All restaurants"
//            binding.categoriesRecyclerView.visibility = View.VISIBLE
            sortRestaurants(allRestaurants.value as MutableList<RestaurantModel>, sortType)
            hasSelectedSortItemChanged = false
            hasSelectedFilterItemsChanged = false
        }else{
            ///////// filter items is not empty
//            binding.headerTitle.text = "Restaurants"
//            binding.categoriesRecyclerView.visibility = View.GONE
            allRestaurants.value?.forEach { res ->
                filterItems.forEach { item ->

                    if (res.categories.contains(item.lowercase())){
                        if (!filteredList.contains(res)){
                            filteredList.add(res)

                        }
                    }
                }
            }
            sortRestaurants(filteredList, sortType)


        }

        }
    private fun sortRestaurants(filteredList:MutableList<RestaurantModel>, sortType:String){
        when(sortType){
            "Recommended"->{
                filteredRestaurants.value = filteredList
//                initRestaurantsItemRecyclerView(filteredList)
            }
            "Delivery price" ->{

//                  filteredList.sortedBy { it.deliveryPrice.toDouble() }

                filteredList.sortBy { it.deliveryPrice.toDouble() }
//                initRestaurantsItemRecyclerView(filteredList)
            }

            "Delivery time"->{
                filteredList.sortBy{ it.deliveryTime.substring(0,2) }

            }



        }
        filteredRestaurants.value = filteredList


    }


}