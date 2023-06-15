package com.example.mealmoverskotlin.domain.usecases.authenticationPageUseCases

import com.example.mealmoverskotlin.data.models.UserModel
import com.google.common.truth.Truth
import org.junit.Test

class ValidateRegisterUserTest{
    private val validateRegisterUser = ValidateRegisterUser()


    @Test
    fun `passwords dont matchR return false`(){
        val result = validateRegisterUser.invoke(UserModel(
            email = "test@Gmail.com",
            password = "123",
            reapeatedPassword = "12"
        ))
        result.let {

        }
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `passwords match returns true`(){
        val result = validateRegisterUser.invoke(UserModel(
            email = "test@Gmail.com",
            password = "Anas.ash123456@! ",
            reapeatedPassword = "Anas.ash123456@!"
        ))
        Truth.assertThat(result).isTrue()
    }


}