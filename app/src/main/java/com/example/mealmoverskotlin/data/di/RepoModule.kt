package com.example.mealmoverskotlin.data.di

import com.example.mealmoverskotlin.domain.repositoryImpl.MainRepository
import com.example.mealmoverskotlin.domain.repositoryImpl.OrderRepositoryImpl
import com.example.mealmoverskotlin.domain.repositoryImpl.SharedPreferencesRepositoryImpl
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
import com.example.mealmoverskotlin.domain.repositorylnterfaces.OrderRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.SharedPreferencesRepository
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
    abstract fun provideRepo(repo: MainRepository): MainRepositoryInterface

    @Binds
    @Singleton
    abstract fun provideOrderRepo(repo: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun provideSharedPrefRepo(repo: SharedPreferencesRepositoryImpl): SharedPreferencesRepository

}