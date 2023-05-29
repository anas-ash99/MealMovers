package com.example.mealmoverskotlin.domain.repositoryImpl

import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.example.mealmoverskotlin.data.apis.OrderApi
import com.example.mealmoverskotlin.data.apis.RestaurantsApi
import com.example.mealmoverskotlin.data.apis.UserApi
import com.example.mealmoverskotlin.data.dataStates.DataState
import com.example.mealmoverskotlin.data.events.AddRestaurantToFavouritesEvent
import com.example.mealmoverskotlin.data.models.*
import com.example.mealmoverskotlin.shared.RetrofitInterface
import com.example.mealmoverskotlin.domain.google.OnDone
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor (
   private val restaurantsApi: RestaurantsApi,
   private val orderApi: OrderApi,
   private val userApi: UserApi

): RestaurantRepositoryInterface {
    override suspend fun getAllRestaurants(): Flow<DataState<MutableList<RestaurantModel>>> = flow {
        emit(DataState.Loading)
        try {
            val res =  restaurantsApi.getAllRestaurants().awaitResponse()
            if (res.isSuccessful){
                emit(DataState.Success(res.body()!!))
            }else{
                throw Exception(res.message())

            }


        }catch (e:Exception){
            Log.e("restaurants", e.toString())
            emit(DataState.Error(e))
        }

    }

    override fun updateLoggedInUser(mPrefs: SharedPreferences, user: UserModel) {
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString("LoggedInUser", json)
        prefsEditor.commit()
    }

    override fun getLoggedInUser(mPrefs: SharedPreferences): UserModel? {
        val gson = Gson()
        val json = mPrefs.getString("LoggedInUser", "")
        return gson.fromJson(json, UserModel::class.java)
    }

    override fun getUserAddress(mPrefs: SharedPreferences): AddressModel? {
        val gson = Gson()
        val json = mPrefs.getString("USER_ADDRESS", "")

        return gson.fromJson(json, AddressModel::class.java)
    }

    override fun updateUserAddress(mPrefs: SharedPreferences, address: AddressModel) {
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(address)
        prefsEditor.putString("USER_ADDRESS", json)
        prefsEditor.commit()
    }

    override fun deleteLoggedInUser(mPrefs: SharedPreferences) {
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.remove("LoggedInUser")
            .commit()
    }

    override fun deleteUserAddress(mPrefs: SharedPreferences) {
        try {

            val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
            prefsEditor.remove("USER_ADDRESS")
                .commit()

        }catch (e:Exception){
            Log.e("tag","Delete address", e )
        }
    }

    override suspend fun createNewOrder(order:OrderModel): Flow<DataState<OrderModel>> = flow {
        emit(DataState.Loading)
        try {
            val res = orderApi.createOrder(order).awaitResponse()
            if (res.code() == 200){
               emit(DataState.Success(res.body()!!))
            }else{
                throw Exception(res.message())
            }

        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("createOrder", e.toString())
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

    override suspend fun getOrderById(id: String, onLoadingDone: RetrofitInterface) {
        try {
            orderApi.getOrderById(id).enqueue(object : Callback<OrderModel?> {
                override fun onResponse(call: Call<OrderModel?>, response: Response<OrderModel?>) {
                    onLoadingDone.onSuccess(response.body()!!)
                }

                override fun onFailure(call: Call<OrderModel?>, t: Throwable) {
                    onLoadingDone.onError(Exception(t.message, t))
                }
            })
        }catch (e:Exception){
            onLoadingDone.onError(e)
            Log.e("order", e.message!!,e )
        }
    }

    override suspend fun getRestaurantById(id: String, onLoadingDone: RetrofitInterface) {
        try {
            restaurantsApi.getRestaurantById(id).enqueue(object : Callback<RestaurantModel?> {
                override fun onResponse(
                    call: Call<RestaurantModel?>,
                    response: Response<RestaurantModel?>
                ) {
                    onLoadingDone.onSuccess(response.body()!!)
                }

                override fun onFailure(call: Call<RestaurantModel?>, t: Throwable) {
                    onLoadingDone.onError(Exception(t.message, t))
                }
            })
        }catch (e:Exception){
            onLoadingDone.onError(e)
            Log.e("order", e.message!!,e )
        }
    }


    override suspend fun getAllRestaurants2(callBack: (restaurants: List<RestaurantModel>?, error: Exception?) -> Unit) {
        try {
            val res =  restaurantsApi.getAllRestaurants().awaitResponse()
            if (res.isSuccessful){
                 callBack(res.body()!!, null)
            }else{
                throw Exception(res.message())

            }

        }catch (e:Exception){
            Log.e("restaurants", e.message!!,e )
            callBack(null,e)
        }
    }




    override suspend fun getOrdersFoUser(id: String, callBack: OnDone) {
        try {
            val res = orderApi.getOrdersForUser(id).awaitResponse()
            if (res.isSuccessful){
                callBack.onLoadingDone(res.body())
            }


        }catch (e:Exception){
            Log.e("orders", e.message!!,e )
            callBack.onError(e)
        }
    }



}