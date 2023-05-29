package com.example.mealmoverskotlin.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.mealmoverskotlin.ui.dialogs.RestaurantsFilterDialog
import com.example.mealmoverskotlin.domain.firebase.FireStoreUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return  context
    }
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

//    @Singleton
//    @Provides
//    fun provideDialog(@ApplicationContext context: Context):RestaurantsFilterDialog {
//        return RestaurantsFilterDialog(context)
//    }
}