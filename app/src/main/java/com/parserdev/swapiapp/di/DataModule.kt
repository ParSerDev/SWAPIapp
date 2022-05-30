package com.parserdev.swapiapp.di

import com.parserdev.swapiapp.data.SWLibraryRepository
import com.parserdev.swapiapp.data.repository.SWLibraryRepositoryImpl
import com.parserdev.swapiapp.data.storage.remote.retrofit.RetrofitSWInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideSWLibraryRepository(retrofitSWInstance: RetrofitSWInstance): SWLibraryRepository {
        return SWLibraryRepositoryImpl(retrofitSWInstance = retrofitSWInstance)
    }

    @Provides
    @Singleton
    fun provideRetrofitSWInstance(): RetrofitSWInstance {
        return RetrofitSWInstance
    }

}