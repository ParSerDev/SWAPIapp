package com.parserdev.swapiapp.features.library.presentation.details

import androidx.lifecycle.*
import com.parserdev.swapiapp.data.repository.LibraryRepository
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.domain.entities.LibraryCategory
import com.parserdev.swapiapp.domain.network.NetworkResult
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val repository: LibraryRepository
) : ViewModel() {

    private val _item = MutableLiveData<NetworkResult<DetailsItem>>()
    val item: LiveData<NetworkResult<DetailsItem>> = _item

    val accept: suspend (DetailsAction) -> Unit = { action ->
        when (action) {
            is DetailsAction.LoadItem -> {
                repository.getDetailsItem(
                    url = action.url,
                    libraryCategory = action.libraryCategory
                ).collectLatest {
                    _item.value = it
                }
            }
        }
    }
    fun clean() {
        _item.value = NetworkResult.Loading()
    }
}

sealed class DetailsAction {
    data class LoadItem(
        val url: String,
        val libraryCategory: LibraryCategory
    ) : DetailsAction()
}