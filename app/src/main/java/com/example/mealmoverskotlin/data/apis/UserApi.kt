package com.example.mealmoverskotlin.data.apis

import com.example.mealmoverskotlin.data.models.RestaurantModel
import com.example.mealmoverskotlin.data.models.SignInResponse
import com.example.mealmoverskotlin.data.models.UserModel
import com.google.firebase.firestore.auth.User
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {




    @GET("user/get_user_by_id/{id}")
    suspend fun getUserById(@Path("id") userId:String): UserModel
    @POST("/user/create_user")
    fun createNewUser(@Body user: UserModel, @Header("Authorization") authHeader:String):Call<JSONObject>

    @POST("/user/signin_user")
    fun signinUser(@Header("Authorization") authHeader:String):Call<SignInResponse?>
    @PUT("/user/add_restaurant_to_favourites/{userId}/{resId}")
    suspend fun addOrDeleteFavourites(@Path("userId") userId:String,@Path("resId") restaurantId: String): UserModel
}