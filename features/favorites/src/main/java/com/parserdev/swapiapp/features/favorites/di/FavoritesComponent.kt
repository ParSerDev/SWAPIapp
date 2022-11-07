package com.parserdev.swapiapp.features.favorites.di

import com.parserdev.swapiapp.features.favorites.di.scopes.FavoritesScope
import com.parserdev.swapiapp.features.favorites.presentation.FavoritesFragment
import dagger.Subcomponent

@FavoritesScope
@Subcomponent
interface FavoritesComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FavoritesComponent
    }

    fun inject(favoritesFragment: FavoritesFragment)
}