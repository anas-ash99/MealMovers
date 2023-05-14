package com.example.mealmoverskotlin.data.di

import com.example.mealmoverskotlin.domain.repositoryImpl.MainRepository
import com.example.mealmoverskotlin.domain.repositorylnterfaces.MainRepositoryInterface
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

}