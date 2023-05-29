package com.example.mealmoverskotlin.domain.viewModels

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.events.ValidateCreateNewUserEvent
import com.example.mealmoverskotlin.data.models.SignInResponse
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.databinding.ActivityAuthenticationBinding
import com.example.mealmoverskotlin.databinding.FragmentLoginBinding
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.UserRepository
import com.example.mealmoverskotlin.shared.DataHolder
import com.example.mealmoverskotlin.ui.address.AddressActivity
import com.example.mealmoverskotlin.ui.authentication.AuthenticationActivity
import com.example.mealmoverskotlin.ui.authentication.LoginFragment
import com.example.mealmoverskotlin.ui.authentication.SignUpFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthenticationPageViewModel @Inject constructor(
    private val userRepo: UserRepository,

    private val sharedPreferencesRepo: SharedPreferencesRepository
    ): ViewModel() {

    val validateCreateNewUserEvent by lazy {
       MutableLiveData<ValidateCreateNewUserEvent>()
    }
    var user:UserModel = UserModel()
    val createUserResponse by lazy {
        MutableLiveData<DataState<Int>>()
    }


    val signinUserResponse by lazy {
        MutableLiveData<DataState<SignInResponse?>>()
    }




    fun validateOnCreateUser(){
        if (user.password != user.reapeatedPassword){
            validateCreateNewUserEvent.value = ValidateCreateNewUserEvent.PASSWORDS_DONT_MATCH
        }else if (!user.email.contains("@")) {
         validateCreateNewUserEvent.value = ValidateCreateNewUserEvent.EMAIL_NOT_VALID
        }
        else{
            createNewUser()
        }


    }







    fun signInUser(){
        viewModelScope.launch {
            userRepo.signinUser(user).onEach {
                signinUserResponse.value = it
            }.launchIn(viewModelScope)
        }

    }

    private fun createNewUser(){

        viewModelScope.launch {
            userRepo.createNewUser(user).onEach {
                createUserResponse.value = it
            }.launchIn(viewModelScope)




        }
    }


   fun updateLoggedInUser(user: UserModel){
       viewModelScope.launch {
           sharedPreferencesRepo.updateLoggedInUser(user)
           DataHolder.loggedInUser = user
       }

   }








}