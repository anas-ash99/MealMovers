package com.example.mealmoverskotlin.domain.viewModels



import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.shared.Categories
import com.example.mealmoverskotlin.shared.DataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("SetTextI18n")
@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val repo: RestaurantRepositoryInterface,
    private val sharedPreferencesRepository: SharedPreferencesRepository

):ViewModel() {

    var currentCategory:Categories = Categories.ALL

    var hasSelectedFilterItemsChanged:Boolean = false
    var hasSelectedSortItemChanged:Boolean = false

    val _restaurants by lazy {
        MutableLiveData<DataState<List<RestaurantModel>>>()
    }
    val filteredRestaurants by lazy {
        MutableLiveData<List<RestaurantModel>>()
    }
    val loading by lazy {
        MutableLiveData<Boolean>()
    }
    val allRestaurants by lazy {
        MutableLiveData<List<RestaurantModel>>()
    }

    val loggedInUser by lazy {
        MutableLiveData<UserModel>()
    }

    val isFilterApplied by lazy {
        MutableLiveData<Boolean>()
    }
    val userAddress by lazy {
        MutableLiveData<AddressModel>()
    }

    var selectedItems = mutableListOf<String>()
    var sortTypeVM = "Recommended"

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
        viewModelScope.launch {
            sharedPreferencesRepository.updateLoggedInUser(user)

        }
    }



    fun getUserAddress(){

        viewModelScope.launch {
            DataHolder.userAddress = sharedPreferencesRepository.getUserAddress()
            userAddress.value = DataHolder.userAddress
        }

    }




    fun onDialogApplyClick(sortType:String){

        hasSelectedFilterItemsChanged = selectedItems.isNotEmpty()!!
        hasSelectedSortItemChanged = sortType != "Recommended"
        var filteredList = mutableListOf<RestaurantModel>()
        if (selectedItems.isEmpty()!!){

            sortRestaurants(allRestaurants.value as MutableList<RestaurantModel>, sortType)

        }else{
            ///////// filter items is not empty

            allRestaurants.value?.forEach { res ->
                selectedItems.forEach { item ->
                    if (res.categories.contains(item.lowercase())){
                        if (!filteredList.contains(res)){
                            filteredList.add(res)

                        }
                    }
                }
            }
            sortRestaurants(filteredList, sortType)
            isFilterApplied.value = true

        }

        }
    private fun sortRestaurants(filteredList:MutableList<RestaurantModel>, sortType:String){
        when(sortType){
            "Recommended"->{
                filteredRestaurants.value = filteredList
//                initRestaurantsItemRecyclerView(filteredList)
                 if (selectedItems.isEmpty()) isFilterApplied.value =false
            }
            "Delivery price" ->{

//                  filteredList.sortedBy { it.deliveryPrice.toDouble() }
                isFilterApplied.value = true
                filteredList.sortBy { it.deliveryPrice.toDouble() }
//                initRestaurantsItemRecyclerView(filteredList)
            }

            "Delivery time"->{
                isFilterApplied.value = true
                filteredList.sortBy{ it.deliveryTime.substring(0,2) }

            }



        }
        filteredRestaurants.value = filteredList


    }


}