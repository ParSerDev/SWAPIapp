package com.parserdev.swapiapp.features.library.presentation.library.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class LibraryLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LibraryLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LibraryLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LibraryLoadStateViewHolder {
        return LibraryLoadStateViewHolder.create(parent, retry)
    }

}
