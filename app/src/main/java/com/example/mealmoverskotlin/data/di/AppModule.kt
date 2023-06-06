package com.example.mealmoverskotlin.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.mealmoverskotlin.data.apis.GoogleApi
import com.example.mealmoverskotlin.data.apis.OrderApi
import com.example.mealmoverskotlin.data.apis.RestaurantsApi
import com.example.mealmoverskotlin.domain.firebase.FireStoreUseCase
import com.example.mealmoverskotlin.domain.google.GoogleAddressAutoComplete
import com.example.mealmoverskotlin.domain.google.GoogleGeocoding
import com.example.mealmoverskotlin.domain.repositoryImpl.RestaurantRepositoryImpl
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.example.mealmoverskotlin.domain.usecases.restaurantPageUseCases.CheckIfRestaurantOpen
import com.example.mealmoverskotlin.domain.usecases.confirnOrderPage.SetScheduleTimeArray
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


//
//    @Singleton
//    @Provides
//    fun provideContext(@ApplicationContext context: Context): Context {
//        return  context
//    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context
    @Singleton
    @Provides
    fun provideFirebase(): FireStoreUseCase {
        return  FireStoreUseCase()
    }


    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAddressAutoComplete(@ApplicationContext context: Context): GoogleAddressAutoComplete {
        return GoogleAddressAutoComplete(context)
    }


    @Singleton
    @Provides
    fun provideGoogleGeocoding(api:GoogleApi): GoogleGeocoding {
        return GoogleGeocoding(api)
    }

    @Singleton
    @Provides
    fun provideSetScheduleTime(): SetScheduleTimeArray {
        return SetScheduleTimeArray()
    }

    @Singleton
    @Provides
    fun provideCheckIfRestaurantOpen(): CheckIfRestaurantOpen {
        return CheckIfRestaurantOpen()
    }


//    @Singleton
//    @Provides
//    fun provideRepo(resApi:RestaurantsApi, orderApi:OrderApi): RestaurantRepositoryImpl {
//        return  RestaurantRepositoryImpl(resApi,orderApi)
//    }

//    @Singleton
//    @Provides
//    fun provideDialog(@ApplicationContext context: Context):RestaurantsFilterDialog {
//        return RestaurantsFilterDialog(context)
//    }
}