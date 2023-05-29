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
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.events.ValidateCreateNewUserEvent
import com.example.mealmoverskotlin.data.models.UserModel
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
        observeCreateUserResponse()
        observeEmailValidation()
        return binding.root
    }

    private fun observeEmailValidation() {
        viewModel.validateCreateNewUserEvent.observe(requireActivity()){
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeCreateUserResponse() {
        viewModel.createUserResponse.observe(requireActivity()) {

            when(it){
                is DataState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data == 200) {
                        Toast.makeText(activity, "successful", Toast.LENGTH_SHORT).show()
                        switchToLoginFragment()
                    }else if (it.data == 406){
                        Toast.makeText(activity, "Email already exist", Toast.LENGTH_SHORT).show()

                    }
                    viewModel.createUserResponse.value = null

                }
                is DataState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

            }
        }
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
        switchToLoginFragment()
     }
    }



    private fun validateOnCreateUser(){

        if (viewModel.user.email == "" ) binding.emailEditText.error = "Email is required"
        if (viewModel.user.fullName == "") binding.passwordEditText.error = "Name is required"
        if (viewModel.user.password =="" ) binding.nameEditText.error = "Password is required"
        if (viewModel.user.reapeatedPassword =="" ) binding.repeatPasswordEditText.error = "Password is required"

        else{

            viewModel.validateOnCreateUser()
        }

    }

    private fun switchToLoginFragment(){

        viewModel.user = UserModel()
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down)
            replace(R.id.fragment_layout, LoginFragment(), "LOGIN")
        }

    }


}