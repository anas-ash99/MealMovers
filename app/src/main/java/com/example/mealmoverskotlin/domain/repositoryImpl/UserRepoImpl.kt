package com.example.mealmoverskotlin.domain.repositoryImpl

import android.util.Base64
import android.util.Log
import com.example.mealmoverskotlin.data.apis.UserApi
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.events.AddRestaurantToFavouritesEvent
import com.example.mealmoverskotlin.data.models.SignInResponse
import com.example.mealmoverskotlin.data.models.UserModel
import com.example.mealmoverskotlin.domain.repositorylnterfaces.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val userApi: UserApi
    ):UserRepository {


    override suspend fun addRestaurantToFavourites(
        userId: String,
        restaurantId: String
    ): Flow<AddRestaurantToFavouritesEvent<UserModel>> = flow {
        try {
            emit(AddRestaurantToFavouritesEvent.Loading)
            val res = userApi.addOrDeleteFavourites(userId, restaurantId)

            emit(AddRestaurantToFavouritesEvent.Success(res))

        }catch (e:Exception){
            Log.e("add to Favourites", e.message, e)
            emit(AddRestaurantToFavouritesEvent.Error(e))
        }
    }

    override suspend fun signinUser(user: UserModel): Flow<DataState<SignInResponse?>> = flow {

        emit(DataState.Loading)
        try {

            val base:String = user.email + ":" + user.password
            val authHeader ="Basic " + Base64.encodeToString(base.encodeToByteArray(), Base64.NO_WRAP)
            val res = userApi.signinUser(authHeader).awaitResponse()

            if (res.isSuccessful ){
                emit(DataState.Success(res.body()!!))
            }

        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("signin", e.toString())
            println(e)
        }
    }
    override suspend fun createNewUser(user: UserModel): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)
        try {
            var base:String = user.email + ":" + user.password
            var authHeader ="Basic " + Base64.encodeToString(base.encodeToByteArray(), Base64.NO_WRAP)

            val res =  userApi.createNewUser(user, authHeader).awaitResponse()



            if (res.code() == 200 || res.code() == 406 ){
                emit(DataState.Success(res.code()))
            }else {
                emit(DataState.Error(Exception()))
            }

        }catch (e:Exception){
            println(e)
            Log.e("createUser", e.toString())
            emit(DataState.Error(e))
        }
    }
}