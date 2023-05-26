package com.example.mealmoverskotlin.domain.repositorylnterfaces

import android.content.SharedPreferences
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.UserModel

interface SharedPreferencesRepository {

    suspend fun updateLoggedInUser(user: UserModel)
    suspend fun getLoggedInUser(): UserModel?
    suspend fun deleteLoggedInUser()
    suspend fun deleteUserAddress()
    suspend fun getUserAddress(): AddressModel?
    suspend fun updateUserAddress( address: AddressModel)


}