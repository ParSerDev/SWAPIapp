package com.parserdev.swapiapp.features.library.presentation.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.parserdev.characters.databinding.LibraryViewItemBinding
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.entities.LibraryCategory

class LibraryAdapter(private val clickListener: (String, LibraryCategory) -> Unit) :
    PagingDataAdapter<LibraryItem, LibraryItemViewHolder>(diffCallback = DiffCallBack()) {

    class DiffCallBack : DiffUtil.ItemCallback<LibraryItem>() {
        override fun areItemsTheSame(
            oldItem: LibraryItem,
            newItem: LibraryItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LibraryItem,
            newItem: LibraryItem
        ): Boolean {
            return oldItem.title == newItem.title
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryItemViewHolder {
        return LibraryItemViewHolder(
            LibraryViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener = clickListener
        )
    }

    override fun onBindViewHolder(holder: LibraryItemViewHolder, position: Int) {
        val libraryItem = getItem(position = position)
        holder.bind(libraryItem)
    }


}