package com.example.mealmoverskotlin.domain.viewModels

import android.content.Context
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
import com.example.mealmoverskotlin.data.models.SignInResponse
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.databinding.ActivityAuthenticationBinding
import com.example.mealmoverskotlin.databinding.FragmentLoginBinding
import com.example.mealmoverskotlin.databinding.FragmentSignUpBinding
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
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
    private val repository: MainRepositoryInterface
    ): ViewModel() {

    var logInBinding:FragmentLoginBinding? = null
    var signUpBinding:FragmentSignUpBinding? = null
    lateinit var mainBinding:ActivityAuthenticationBinding
    private lateinit var activity:AuthenticationActivity
    var user:UserModel = UserModel()
    private val createUserResponse:MutableLiveData<DataState<Int>> by lazy {
        MutableLiveData<DataState<Int>>()
    }


    private val signinUserResponse:MutableLiveData<DataState<SignInResponse?>> by lazy {
        MutableLiveData<DataState<SignInResponse?>>()
    }
  init {

  }
    fun init(activity: AuthenticationActivity,mainBinding:ActivityAuthenticationBinding ){
        this.activity = activity
        this.mainBinding = mainBinding

        /////////// set transparent statusBar
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)



    }

    fun initBinding(logInBinding:FragmentLoginBinding){
        this.logInBinding = logInBinding
    }



    fun switchToLoginFragment(){

        user = UserModel()
        activity.supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down)
            replace(R.id.fragment_layout, LoginFragment(), "LOGIN")
        }

    }


    fun switchToSignUpFragment(){

        user = UserModel()
        activity.supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
            replace(R.id.fragment_layout, SignUpFragment(), "sign up")
        }


    }



    fun signInUser(){
        mainBinding.progressBar.visibility = View.VISIBLE
        viewModelScope.launch {
            repository.signinUser(user).onEach {
                signinUserResponse.value = it
            }.launchIn(viewModelScope)
        }

        signinUserResponse.observe(activity, Observer {
            when(it){
                is DataState.Success -> {
                    mainBinding.progressBar.visibility = View.GONE
                    if (it.data?.message =="Login success"){

                        updateLoggedInUser(it.data.user!!)
                        val i = Intent(activity, AddressActivity::class.java)
                        i.putExtra("isAfterSignUp", true)
                        activity.startActivity(i)

                        activity.finish()

                    }
                    Toast.makeText(activity, it.data?.message, Toast.LENGTH_SHORT).show()
                    signinUserResponse.value = null
                }

                is DataState.Error-> {
                    Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()
                    signinUserResponse.value = null
                    mainBinding.progressBar.visibility = View.GONE
                }
                else -> {

                }

            }


        })




    }

    fun createNewUser(){
        mainBinding.progressBar.visibility = View.VISIBLE
        viewModelScope.launch {
            repository.createNewUser(user).onEach {
                createUserResponse.value = it
            }.launchIn(viewModelScope)

            createUserResponse.observe(activity, Observer {

                when(it){
                    is DataState.Success -> {
                        mainBinding.progressBar.visibility = View.GONE
                        if (it.data == 200) {
                            Toast.makeText(activity, "successful", Toast.LENGTH_SHORT).show()
                            switchToLoginFragment()
                        }else if (it.data == 406){
                            Toast.makeText(activity, "Email already exist", Toast.LENGTH_SHORT).show()

                        }
                        createUserResponse.value = null

                    }
                    is DataState.Error -> {
                        mainBinding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Loading -> {

                    }
                    else -> {}
                }
            })


        }
    }


   fun updateLoggedInUser(user: UserModel){
       repository.updateLoggedInUser(activity.getSharedPreferences("PROFILE",Context.MODE_PRIVATE), user)
       DataHolder.loggedInUser = user

   }








}