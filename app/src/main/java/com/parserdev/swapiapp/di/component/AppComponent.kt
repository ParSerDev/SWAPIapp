package com.parserdev.swapiapp.di.component

import android.content.Context
import com.parserdev.swapiapp.data.di.module.DatabaseModule
import com.parserdev.swapiapp.data.di.module.NetworkModule
import com.parserdev.swapiapp.di.app.AppSubcomponents
import com.parserdev.swapiapp.features.favorites.di.FavoritesComponent
import com.parserdev.swapiapp.features.library.di.LibraryComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class, DatabaseModule::class,
        AppSubcomponents::class]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun libraryComponent(): LibraryComponent.Factory
    fun favoritesComponent(): FavoritesComponent.Factory
}