package com.example.mealmoverskotlin.domain.repositorylnterfaces

import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.events.AddRestaurantToFavouritesEvent
import com.example.mealmoverskotlin.data.models.SignInResponse
import com.example.mealmoverskotlin.data.models.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun createNewUser(user: UserModel): Flow<DataState<Int>>
    suspend fun signinUser(user: UserModel):Flow<DataState<SignInResponse?>>
    suspend fun addRestaurantToFavourites(userId:String,restaurantId:String ): Flow<AddRestaurantToFavouritesEvent<UserModel>>
}