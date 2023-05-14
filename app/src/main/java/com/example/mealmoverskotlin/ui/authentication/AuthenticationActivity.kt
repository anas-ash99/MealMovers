package com.example.mealmoverskotlin.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.example.mealmoverskotlin.R
import com.example.mealmoverskotlin.databinding.ActivityAuthenticationBinding
import com.example.mealmoverskotlin.domain.viewModels.AuthenticationPageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAuthenticationBinding
    private val viewModel:AuthenticationPageViewModel  by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_authentication)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, LoginFragment(), "LOGIN").commit()
        }

        getSharedPreferences("", MODE_PRIVATE)

        viewModel.init(this, binding)
    }
}