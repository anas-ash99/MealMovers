package com.example.mealmoverskotlin.domain.repositoryImpl

import android.content.SharedPreferences
import android.util.Log
import com.example.mealmoverskotlin.data.models.AddressModel
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.google.gson.Gson
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(
    val mPrefs: SharedPreferences
) : SharedPreferencesRepository {
    override suspend fun updateLoggedInUser( user: UserModel) {
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString("LoggedInUser", json)
        prefsEditor.commit()
    }

    override suspend fun getLoggedInUser(): UserModel? {
        val gson = Gson()
        val json = mPrefs.getString("LoggedInUser", "")
        return gson.fromJson(json, UserModel::class.java)
    }

    override suspend fun deleteLoggedInUser() {
        try {

            val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
            prefsEditor.remove("USER_ADDRESS")
                .commit()

        }catch (e:Exception){
            Log.e("tag","Delete address", e )
        }
    }

    override suspend fun deleteUserAddress() {
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.remove("LoggedInUser")
            .commit()
    }

    override suspend fun getUserAddress(): AddressModel? {
        val gson = Gson()
        val json = mPrefs.getString("USER_ADDRESS", "")

        return gson.fromJson(json, AddressModel::class.java)
    }

    override suspend fun updateUserAddress(address: AddressModel) {
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(address)
        prefsEditor.putString("USER_ADDRESS", json)
        prefsEditor.commit()
    }
}