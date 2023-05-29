package com.example.mealmoverskotlin.data.di

import com.example.mealmoverskotlin.domain.repositoryImpl.RestaurantRepositoryImpl
import com.example.mealmoverskotlin.domain.repositoryImpl.OrderRepositoryImpl
import com.example.mealmoverskotlin.domain.repositoryImpl.SharedPreferencesRepositoryImpl
import com.example.mealmoverskotlin.domain.repositoryImpl.UserRepoImpl
import com.example.mealmoverskotlin.domain.repositorylnterfaces.RestaurantRepositoryInterface
import com.example.mealmoverskotlin.domain.repositorylnterfaces.OrderRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun provideRepo(repo: RestaurantRepositoryImpl): RestaurantRepositoryInterface

    @Binds
    @Singleton
    abstract fun provideOrderRepo(repo: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun provideUserRepo(repo: UserRepoImpl): UserRepository

    @Binds
    @Singleton
    abstract fun provideSharedPrefRepo(repo: SharedPreferencesRepositoryImpl): SharedPreferencesRepository

}