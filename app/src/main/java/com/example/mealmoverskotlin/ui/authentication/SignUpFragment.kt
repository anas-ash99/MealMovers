package com.example.mealmoverskotlin.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.FragmentLoginBinding
import com.example.mealmoverskotlin.databinding.FragmentSignUpBinding
import com.example.mealmoverskotlin.domain.viewModels.AuthenticationPageViewModel


class SignUpFragment : Fragment() {

   private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel:AuthenticationPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        onLoginTextClick()
        viewModel = ViewModelProvider(requireActivity())[AuthenticationPageViewModel::class.java]
        takeInput()
        onSignUpButtonClick()
        return binding.root
    }


    private fun takeInput(){
        binding.emailEditText.addTextChangedListener {
            viewModel.user.email = it?.toString()?.trim()!!
        }
        binding.passwordEditText.addTextChangedListener {
            viewModel.user.password = it?.toString()?.trim()!!
        }
        binding.nameEditText.addTextChangedListener {
            viewModel.user.fullName = it?.toString()?.trim()!!
        }

        binding.repeatPasswordEditText.addTextChangedListener {
            viewModel.user.reapeatedPassword = it?.toString()?.trim()!!
        }

    }

    private fun onSignUpButtonClick(){
        binding.button.setOnClickListener {
            validateOnCreateUser()
        }
    }

    private fun onLoginTextClick() {
     binding.loginTextView.setOnClickListener {
         viewModel.switchToLoginFragment()
     }
    }



    private fun validateOnCreateUser(){

        if (viewModel.user.email == "" ) binding.emailEditText.error = "Email is required"
        if (viewModel.user.fullName == "") binding.passwordEditText.error = "Name is required"
        if (viewModel.user.password =="" ) binding.nameEditText.error = "Password is required"
        if (viewModel.user.reapeatedPassword =="" ) binding.repeatPasswordEditText.error = "Password is required"

        else{

            if (viewModel.user.password != viewModel.user.reapeatedPassword){
                Toast.makeText(requireContext(), "Passwords don't match", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.createNewUser()
            }
        }

    }


}