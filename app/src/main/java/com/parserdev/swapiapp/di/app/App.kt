package com.parserdev.swapiapp.di.app

import android.app.Application
import com.parserdev.swapiapp.di.component.AppComponent
import com.parserdev.swapiapp.di.component.DaggerAppComponent
import com.parserdev.swapiapp.features.favorites.di.FavoritesComponent
import com.parserdev.swapiapp.features.favorites.di.FavoritesComponentProvider
import com.parserdev.swapiapp.features.library.di.LibraryComponent
import com.parserdev.swapiapp.features.library.di.LibraryComponentProvider

open class App : Application(), LibraryComponentProvider, FavoritesComponentProvider {
    private val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    override fun provideLibraryComponent(): LibraryComponent {
        return appComponent.libraryComponent().create()
    }

    override fun provideFavoritesComponent(): FavoritesComponent {
        return appComponent.favoritesComponent().create()
    }
}