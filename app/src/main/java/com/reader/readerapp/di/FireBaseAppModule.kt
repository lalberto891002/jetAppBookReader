package com.reader.readerapp.di

import com.reader.readerapp.network.FireBaseNetwork
import com.reader.readerapp.network.FireBaseNetworkImpl
import com.reader.readerapp.repository.FireBaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FireBaseAppModule {
    @Singleton
    @Binds
    abstract fun provideSaveBooksNetworkApi(impl: FireBaseNetworkImpl): FireBaseNetwork
}