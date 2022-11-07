package com.parserdev.swapiapp.features.library.presentation.library.adapter

import com.example.ui_components.R
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.parserdev.characters.databinding.LibraryViewItemBinding
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.entities.LibraryCategory

class LibraryItemViewHolder(private val libraryViewItemBinding: LibraryViewItemBinding,
val clickListener: (String, LibraryCategory) -> Unit) :
    RecyclerView.ViewHolder(libraryViewItemBinding.root) {

    fun bind(item: LibraryItem?) {
        libraryViewItemBinding.apply {
            if (item != null) {
                textTitle.text = item.title
                imageIcon.setImageResource(
                    when (item.category) {
                        LibraryCategory.CHARACTER -> R.drawable.ic_character
                        LibraryCategory.FILM -> R.drawable.ic_film
                        LibraryCategory.STARSHIP -> R.drawable.ic_starship
                        LibraryCategory.VEHICLE -> R.drawable.ic_vehicle
                        LibraryCategory.SPECIE -> R.drawable.ic_specie
                        LibraryCategory.PLANET -> R.drawable.ic_planet
                        else -> 0
                    }
                )
                layout.setOnClickListener {
                    clickListener(item.url, item.category)
                }
            } else {
                val resources = itemView.resources
                textTitle.text = resources.getString(R.string.loading)
                imageIcon.visibility = View.GONE
            }
        }
    }

}