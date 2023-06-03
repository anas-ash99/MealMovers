package com.example.mealmoverskotlin.data.di

import com.example.mealmoverskotlin.domain.repositoryImpl.*
import com.example.mealmoverskotlin.domain.repositorylnterfaces.*
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
    @Binds
    @Singleton
    abstract fun provideKlarnaRepo(repo: KlarnaRepoImpl): KlarnaRepository
    @Binds
    @Singleton
    abstract fun provideStripeRepo(repo: StripeRepoImpl): StripeRepository

}