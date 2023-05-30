package com.example.mealmoverskotlin.ui.authentication

import android.content.Intent
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
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.databinding.FragmentLoginBinding
import com.example.mealmoverskotlin.domain.viewModels.AuthenticationPageViewModel
import com.example.mealmoverskotlin.shared.KeyboardManger
import com.example.mealmoverskotlin.shared.KeyboardManger.hideSoftKeyboard
import com.example.mealmoverskotlin.ui.address.AddressActivity


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




        observeLoginUserResponse()
        onRegisterClick()
        onLoginButtonClick()
        takeUserInput()
        return binding.root

    }

    private fun observeLoginUserResponse() {

        viewModel.signinUserResponse.observe(requireActivity()){
            when(it){
                is DataState.Success -> {

                    if (it.data?.message =="Login success"){
                        binding.progressBar.visibility = View.GONE
                        viewModel.updateLoggedInUser(it.data.user!!)
                        val i = Intent(requireActivity(), AddressActivity::class.java)
                        i.putExtra("isAfterSignUp", true)
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT).show()
                        viewModel.signinUserResponse.value = null
                        requireActivity().startActivity(i)
                        activity?.finish()

                    }

                }

                is DataState.Error-> {
                    Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()
                    viewModel.signinUserResponse.value = null
                    binding.progressBar.visibility = View.GONE
                }
                DataState.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }

            }


        }
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
           switchToSignUpFragment()
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


    private fun switchToSignUpFragment(){

        viewModel.user = UserModel()
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)

            replace(R.id.fragment_layout, SignUpFragment(), "sign up")
        }


    }

}