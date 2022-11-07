package com.parserdev.swapiapp.di.app

import com.parserdev.swapiapp.features.favorites.di.FavoritesComponent
import com.parserdev.swapiapp.features.library.di.LibraryComponent
import dagger.Module

@Module(
    subcomponents = [
        LibraryComponent::class,
        FavoritesComponent::class
    ]
)
class AppSubcomponents