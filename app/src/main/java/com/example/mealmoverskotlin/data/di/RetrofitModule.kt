package com.example.mealmoverskotlin.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.mealmoverskotlin.data.apis.*
import com.example.mealmoverskotlin.domain.firebase.FireStoreUseCase
import com.example.mealmoverskotlin.shared.Constants
import com.example.mealmoverskotlin.shared.KlarnaConst
import com.google.firebase.firestore.auth.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_heroku)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideRestaurantApi(retrofit: Retrofit = provideRetrofit()):RestaurantsApi{
        return  retrofit.create(RestaurantsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOrderApi(retrofit: Retrofit = provideRetrofit()):OrderApi{
        return  retrofit.create(OrderApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit = provideRetrofit()):UserApi{
        return  retrofit.create(UserApi::class.java)
    }


    @Singleton
    @Provides
    fun provideStripeApi(retrofit: Retrofit = provideRetrofit()):PaymentsApi{
        return  retrofit.create(PaymentsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGoogleApiRetrofit():GoogleApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://maps.googleapis.com/")
            .build()
            .create(GoogleApi::class.java)
    }

    @Singleton
    @Provides
    fun provideKlarnaApi():KlarnaApi{
        return  Retrofit.Builder()
            .baseUrl(KlarnaConst.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KlarnaApi::class.java)
    }




}