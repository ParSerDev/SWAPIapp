package com.parserdev.swapiapp.features.library.presentation.library.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.parserdev.swapiapp.data.repository.LibraryRepository
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.entities.LibraryCategory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel @AssistedInject constructor(
    private val repository: LibraryRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    lateinit var state: StateFlow<LibraryState>
    var pagingDataFlow: Flow<PagingData<LibraryItem>>
    val accept: (LibraryAction) -> Unit

    init {
        val initialCategory: LibraryCategory =
            savedStateHandle[LAST_CATEGORY] ?: LibraryCategory.CHARACTER
        val initialQuery: String = savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle[LAST_QUERY_SCROLLED] ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<LibraryAction>()
        val changedCategory = actionStateFlow
            .filterIsInstance<LibraryAction.ChangeCategory>()
            .distinctUntilChanged()
            .onStart { emit(LibraryAction.ChangeCategory(category = initialCategory)) }
        val searches = actionStateFlow
            .filterIsInstance<LibraryAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(LibraryAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<LibraryAction.Scroll>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(LibraryAction.Scroll(currentQuery = lastQueryScrolled)) }
        state = combine(
            searches,
            queriesScrolled,
            changedCategory
        ) { search, scroll, category ->
            LibraryState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                hasNotScrolledForCurrentSearch = (search.query != scroll.currentQuery || category.category != state.value.category),
                category = category.category
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = LibraryState()
            )
        pagingDataFlow = state
            .flatMapLatest {
                repository.getLibraryItems(query = it.query, category = it.category)
            }
            .cachedIn(viewModelScope)

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
        savedStateHandle[LAST_CATEGORY] = state.value.category
        super.onCleared()
    }

}

sealed class LibraryAction {
    data class Search(val query: String) : LibraryAction()
    data class Scroll(val currentQuery: String) : LibraryAction()
    data class ChangeCategory(val category: LibraryCategory) : LibraryAction()
}

data class LibraryState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false,
    val category: LibraryCategory = LibraryCategory.CHARACTER
)

private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val LAST_CATEGORY: String = "last_category"
private const val DEFAULT_QUERY: String = ""