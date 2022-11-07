package com.parserdev.swapiapp.features.library.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.parserdev.swapiapp.data.repository.LibraryRepository
import javax.inject.Inject

class DetailsViewModelFactory @Inject constructor(
    private val repository: LibraryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository) as T
    }
}