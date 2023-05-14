package com.example.mealmoverskotlin.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.databinding.FragmentLoginBinding
import com.example.mealmoverskotlin.domain.viewModels.AuthenticationPageViewModel
import com.example.mealmoverskotlin.shared.KeyboardManger
import com.example.mealmoverskotlin.shared.KeyboardManger.hideSoftKeyboard


class LoginFragment : Fragment() {

   private lateinit var binding:FragmentLoginBinding
    private lateinit var viewModel:AuthenticationPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(requireActivity())[AuthenticationPageViewModel::class.java]
//        viewModel.logInBinding = binding
       viewModel.initBinding(binding)




        onRegisterClick()
        onLoginButtonClick()
        takeUserInput()
        return binding.root

    }

    private fun takeUserInput() {
        binding.emailEditText.addTextChangedListener {
            viewModel.user.email = it?.toString()?.trim()!!
        }
        binding.passwordEditText.addTextChangedListener {
            viewModel.user.password = it?.toString()?.trim()!!
        }
    }

    private fun onLoginButtonClick() {
        binding.button.setOnClickListener {
            KeyboardManger.hideSoftKeyboard(binding.button, requireActivity())
            validateUserInfo()
        }
    }

    private fun onRegisterClick() {
        binding.registerTextView.setOnClickListener {
            viewModel.switchToSignUpFragment()
        }
    }

    private fun validateUserInfo(){

        if (viewModel.user.email == ""){
            binding.emailEditText.error = "Please enter your email"
        }else if (!viewModel.user.email.contains("@")) binding.emailEditText.error = "Please enter a valid email"

        if (viewModel.user.password == ""){
            binding.passwordEditText.error = "Please enter your password"
        }

        else{
            viewModel.signInUser()
        }
    }


}