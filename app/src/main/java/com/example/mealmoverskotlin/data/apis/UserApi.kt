package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.SignInResponse
import com.example.mealmoverskotlin.data.models.UserModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {




    @POST("/user/create_user")
    fun createNewUser(@Body user: UserModel, @Header("Authorization") authHeader:String):Call<JSONObject>

    @POST("/user/signin_user")
    fun signinUser(@Header("Authorization") authHeader:String):Call<SignInResponse?>
}