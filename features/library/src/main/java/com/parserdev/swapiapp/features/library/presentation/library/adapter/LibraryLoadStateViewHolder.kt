package com.parserdev.swapiapp.features.library.presentation.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.parserdev.characters.R
import com.parserdev.characters.databinding.LibraryLoadStateFooterViewItemBinding
import com.parserdev.swapiapp.domain.UNABLE_TO_CONNECT

class LibraryLoadStateViewHolder(
    private val binding: LibraryLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.buttonRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        binding.apply {
            if (loadState is LoadState.Error) {
                textError.text = UNABLE_TO_CONNECT
            }
            progressBar.isVisible = loadState is LoadState.Loading
            buttonRetry.isVisible = loadState is LoadState.Error
            textError.isVisible = loadState is LoadState.Error
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LibraryLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.library_load_state_footer_view_item, parent, false)
            val binding = LibraryLoadStateFooterViewItemBinding.bind(view)
            return LibraryLoadStateViewHolder(binding, retry)
        }
    }
}