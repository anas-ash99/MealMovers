package com.example.mealmoverskotlin.data.models

import com.google.firebase.installations.remote.InstallationResponse.ResponseCode
import java.time.LocalDateTime

data class UserModel (
    var _id:String? = null,
    var fullName:String = "",
    var email:String = "",
    var password:String = "",
    var reapeatedPassword:String = "",
    var message:String = "",
    var stripeCustomerId:String="",
    var responseCode: Int = 0,
    var created_at:String = LocalDateTime.now().toString(),
    var  favouriteRestaurants:MutableList<String> = mutableListOf()
):java.io.Serializable