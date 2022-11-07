package com.parserdev.swapiapp.features.library.di

import com.parserdev.swapiapp.features.library.di.scopes.LibraryScope
import com.parserdev.swapiapp.features.library.presentation.details.DetailsFragment
import com.parserdev.swapiapp.features.library.presentation.library.LibraryFragment
import dagger.Subcomponent

@LibraryScope
@Subcomponent
interface LibraryComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): LibraryComponent
    }

    fun inject(libraryFragment: LibraryFragment)
    fun inject(detailsFragment: DetailsFragment)
}