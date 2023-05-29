package com.example.mealmoverskotlin.domain.repositorylnterfaces

import android.content.SharedPreferences
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.events.AddRestaurantToFavouritesEvent
import com.example.mealmoverskotlin.data.models.*
import com.example.mealmoverskotlin.shared.RetrofitInterface
import com.example.mealmoverskotlin.domain.google.OnDone
import kotlinx.coroutines.flow.Flow

interface RestaurantRepositoryInterface {

    suspend fun getAllRestaurants(): Flow<DataState<MutableList<RestaurantModel>>>
    suspend fun getAllRestaurants2(callBack:(restaurants:List<RestaurantModel>?,error: Exception?) -> Unit)
    suspend fun createNewOrder(order:OrderModel):Flow<DataState<OrderModel>>
    suspend fun createNewUser(user: UserModel):Flow<DataState<Int>>
    suspend fun signinUser(user: UserModel):Flow<DataState<SignInResponse?>>
    suspend fun getOrderById(id:String, onLoadingDone: RetrofitInterface)
    suspend fun getRestaurantById(id:String, onLoadingDone: RetrofitInterface)
    suspend fun getOrdersFoUser(id:String, callBack: OnDone)
    fun updateLoggedInUser(mPrefs: SharedPreferences, user: UserModel)
    fun getLoggedInUser(mPrefs:SharedPreferences): UserModel?
    fun deleteLoggedInUser(mPrefs:SharedPreferences)
    fun deleteUserAddress(mPrefs:SharedPreferences)
    fun getUserAddress(mPrefs:SharedPreferences): AddressModel?
    fun updateUserAddress(mPrefs: SharedPreferences, address: AddressModel)
}