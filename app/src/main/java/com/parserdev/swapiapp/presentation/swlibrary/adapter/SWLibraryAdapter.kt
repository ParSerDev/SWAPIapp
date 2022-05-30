package com.parserdev.swapiapp.presentation.swlibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.parserdev.swapiapp.databinding.ItemSwLibraryBinding
import com.parserdev.swapiapp.domain.model.SWLibraryItem
import com.parserdev.swapiapp.presentation.swlibrary.SWLibraryFragmentDirections

class SWLibraryAdapter : RecyclerView.Adapter<SWLibraryAdapter.SWLibraryItemViewHolder>() {

    inner class SWLibraryItemViewHolder(val binding: ItemSwLibraryBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<SWLibraryItem>() {
        override fun areItemsTheSame(
            oldItemCharacters: SWLibraryItem,
            newItemCharacters: SWLibraryItem
        ): Boolean {
            return oldItemCharacters.url == newItemCharacters.url
        }

        override fun areContentsTheSame(
            oldItemCharacters: SWLibraryItem,
            newItemCharacters: SWLibraryItem
        ): Boolean {
            return oldItemCharacters == newItemCharacters
        }
    }
    private var differ = AsyncListDiffer(this, diffCallback)

    var itemsList: List<SWLibraryItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun getItemCount() = itemsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SWLibraryItemViewHolder {
        return SWLibraryItemViewHolder(
            ItemSwLibraryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SWLibraryItemViewHolder, position: Int) {
        holder.binding.apply {
            val item = itemsList[position]
            title.text = item.title
            val action =
                SWLibraryFragmentDirections.actionSWLibraryFragmentToSWDetailsFragment(url = item.url)
            layout.setOnClickListener {
                Navigation.findNavController(layout).navigate(action)
            }
        }

    }
}