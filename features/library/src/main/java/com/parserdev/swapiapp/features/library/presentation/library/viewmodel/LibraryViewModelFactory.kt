package com.parserdev.swapiapp.features.library.presentation.library.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.parserdev.swapiapp.data.repository.LibraryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class LibraryViewModelFactory @AssistedInject constructor(
    private val repository: LibraryRepository,
    @Assisted owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = LibraryViewModel(repository, handle) as T
}

@AssistedFactory
interface LibraryViewModelAssistedFactory {
    fun create(owner: SavedStateRegistryOwner): LibraryViewModelFactory
}