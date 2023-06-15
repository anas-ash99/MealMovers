package com.example.mealmoverskotlin.data.di

import android.content.Context
import com.example.mealmoverskotlin.ui.dialogs.RestaurantClosedDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
object ActivityLifeModule {

    @ActivityScoped
    @Provides
    fun provideResClosedDialog(@ApplicationContext context: Context): RestaurantClosedDialog{
        return RestaurantClosedDialog(context)
    }




}