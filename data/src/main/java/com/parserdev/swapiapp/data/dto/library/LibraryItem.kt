package com.parserdev.swapiapp.data.dto.library

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Entity(tableName = "libraryItem")
data class LibraryItem(
    val title: String,
    @PrimaryKey
    val url: String,
    val category: LibraryCategory
)
