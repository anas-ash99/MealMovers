package com.example.mealmoverskotlin.domain.usecases.authenticationPageUseCases

import com.example.mealmoverskotlin.data.models.UserModel

class ValidateRegisterUser {


    fun invoke(user:UserModel):Boolean{
        var isValid = true
        if (!user.email.contains("@")){
            isValid = false
        }

        if (user.password != user.reapeatedPassword ){
            isValid = false
        }
        return isValid
    }
}