package com.parserdev.swapiapp.data.di.module

import android.content.Context
import com.parserdev.swapiapp.data.db.LibraryDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDb(context: Context): LibraryDatabase {
        return LibraryDatabase.getInstance(context)
    }
}