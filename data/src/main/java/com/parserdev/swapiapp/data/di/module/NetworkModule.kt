package com.parserdev.swapiapp.data.di.module

import com.parserdev.swapiapp.data.network.NetworkInstance
import com.parserdev.swapiapp.data.network.retrofit.RetrofitInstance
import dagger.Binds
import dagger.Module

@Module
abstract class NetworkModule {
    @Binds
    abstract fun provideNetworkInstance(instance: RetrofitInstance): NetworkInstance
}