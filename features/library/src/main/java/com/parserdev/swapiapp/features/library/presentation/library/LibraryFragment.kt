package com.parserdev.swapiapp.features.library.presentation.library

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.parserdev.characters.databinding.FragmentLibraryBinding
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.LOCAL_DATA
import com.parserdev.swapiapp.domain.REMOTE_DATA
import com.parserdev.swapiapp.domain.UNABLE_TO_CONNECT
import com.parserdev.swapiapp.domain.entities.LibraryCategory
import com.parserdev.swapiapp.features.library.di.LibraryComponentProvider
import com.parserdev.swapiapp.features.library.presentation.library.adapter.EmptyAdapter
import com.parserdev.swapiapp.features.library.presentation.library.adapter.LibraryAdapter
import com.parserdev.swapiapp.features.library.presentation.library.adapter.LibraryLoadStateAdapter
import com.parserdev.swapiapp.features.library.presentation.library.viewmodel.LibraryAction
import com.parserdev.swapiapp.features.library.presentation.library.viewmodel.LibraryState
import com.parserdev.swapiapp.features.library.presentation.library.viewmodel.LibraryViewModel
import com.parserdev.swapiapp.features.library.presentation.library.viewmodel.LibraryViewModelAssistedFactory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LibraryFragment : Fragment() {

    @Inject
    lateinit var assistedFactory: LibraryViewModelAssistedFactory
    lateinit var libraryViewModel: LibraryViewModel

    lateinit var libraryAdapter: LibraryAdapter

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        libraryAdapter = LibraryAdapter { url, category ->
            val action =
                LibraryFragmentDirections.actionLibraryToDetails(
                    url = url,
                    category = category
                )
            findNavController().navigate(action)
        }
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectLibraryComponent()
        provideViewModel()
        binding.bindState(
            libraryState = libraryViewModel.state,
            pagingData = libraryViewModel.pagingDataFlow,
            libraryActions = libraryViewModel.accept
        )
    }

    private fun FragmentLibraryBinding.bindState(
        libraryState: StateFlow<LibraryState>,
        pagingData: Flow<PagingData<LibraryItem>>,
        libraryActions: (LibraryAction) -> Unit
    ) {
        val header = LibraryLoadStateAdapter { libraryAdapter.retry() }
        val emptyAdapter = EmptyAdapter()
        val concatAdapter = ConcatAdapter(emptyAdapter, libraryAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = LibraryLoadStateAdapter { libraryAdapter.retry() }
        ))
        recyclerView.adapter = concatAdapter
        bindChipGroup(
            onCategoryChanged = libraryActions
        )
        bindSearch(
            libraryState = libraryState,
            onQueryChanged = libraryActions
        )
        bindList(
            header = header,
            libraryAdapter = libraryAdapter,
            libraryState = libraryState,
            pagingData = pagingData,
            onScrollChanged = libraryActions
        )
    }

    private fun FragmentLibraryBinding.bindChipGroup(
        onCategoryChanged: (LibraryAction.ChangeCategory) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            libraryViewModel.state.collectLatest {
                when (it.category) {
                    LibraryCategory.CHARACTER -> chip1.isChecked = true
                    LibraryCategory.FILM -> chip2.isChecked = true
                    LibraryCategory.STARSHIP -> chip3.isChecked = true
                    LibraryCategory.VEHICLE -> chip4.isChecked = true
                    LibraryCategory.SPECIE -> chip5.isChecked = true
                    LibraryCategory.PLANET -> chip6.isChecked = true
                }
            }
        }
        chip1.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onCategoryChanged(LibraryAction.ChangeCategory(category = LibraryCategory.CHARACTER))
            }
        }
        chip2.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onCategoryChanged(LibraryAction.ChangeCategory(category = LibraryCategory.FILM))
            }
        }
        chip3.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onCategoryChanged(LibraryAction.ChangeCategory(category = LibraryCategory.STARSHIP))
            }
        }
        chip4.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onCategoryChanged(LibraryAction.ChangeCategory(category = LibraryCategory.VEHICLE))
            }
        }
        chip5.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onCategoryChanged(LibraryAction.ChangeCategory(category = LibraryCategory.SPECIE))
            }
        }
        chip6.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onCategoryChanged(LibraryAction.ChangeCategory(category = LibraryCategory.PLANET))
            }
        }
    }

    private fun FragmentLibraryBinding.bindSearch(
        libraryState: StateFlow<LibraryState>,
        onQueryChanged: (LibraryAction.Search) -> Unit
    ) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateItemsListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateItemsListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            libraryState
                .map { it.query }
                .distinctUntilChanged()
                .collect(editText::setText)
        }
    }

    private fun FragmentLibraryBinding.updateItemsListFromInput(onQueryChanged: (LibraryAction.Search) -> Unit) {
        editText.text.trim().let {
            recyclerView.scrollToPosition(0)
            onQueryChanged(LibraryAction.Search(query = it.toString()))
        }
    }

    private fun FragmentLibraryBinding.bindList(
        header: LibraryLoadStateAdapter,
        libraryAdapter: LibraryAdapter,
        libraryState: StateFlow<LibraryState>,
        pagingData: Flow<PagingData<LibraryItem>>,
        onScrollChanged: (LibraryAction.Scroll) -> Unit
    ) {
        buttonRetry.setOnClickListener { libraryAdapter.retry() }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(LibraryAction.Scroll(currentQuery = libraryState.value.query))
            }
        })
        val notLoading = libraryAdapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = libraryState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        viewLifecycleOwner.lifecycleScope.launch {
            pagingData.collectLatest(libraryAdapter::submitData)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recyclerView.scrollToPosition(0)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            libraryAdapter.onPagesUpdatedFlow
            libraryAdapter.loadStateFlow.collect { loadState ->
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && libraryAdapter.itemCount > 0 }
                    ?: loadState.prepend
                val isListEmpty =
                    loadState.source.refresh is LoadState.NotLoading && loadState.mediator?.refresh is LoadState.NotLoading && libraryAdapter.itemCount == 0
                textEmpty.isVisible = isListEmpty
                recyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                textStatus.isVisible = loadState.mediator?.refresh !is LoadState.Loading
                if (loadState.mediator?.refresh is LoadState.NotLoading && loadState.source.refresh is LoadState.NotLoading) textStatus.text =
                    REMOTE_DATA
                else if (loadState.mediator?.refresh is LoadState.Error && libraryAdapter.itemCount != 0) textStatus.text =
                    LOCAL_DATA
                else if (loadState.mediator?.refresh is LoadState.Error && libraryAdapter.itemCount == 0) textStatus.text =
                    UNABLE_TO_CONNECT
                buttonRetry.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && libraryAdapter.itemCount == 0
            }
        }
    }

    private fun injectLibraryComponent() {
        val libraryComponent =
            (activity?.application as LibraryComponentProvider).provideLibraryComponent()
        libraryComponent.inject(this)
    }

    private fun provideViewModel() {
        val viewModelFactory = assistedFactory.create(this)
        libraryViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[LibraryViewModel::class.java]
    }

}