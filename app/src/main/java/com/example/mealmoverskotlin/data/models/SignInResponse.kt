package com.example.mealmoverskotlin.data.models

data class SignInResponse(
    var message:String,
    var user: UserModel? = null
)
