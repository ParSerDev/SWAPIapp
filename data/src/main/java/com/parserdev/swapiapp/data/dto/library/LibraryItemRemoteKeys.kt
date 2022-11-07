package com.parserdev.swapiapp.data.dto.library

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Entity(tableName = "library_item_remote_keys")
data class LibraryItemRemoteKeys(
    @PrimaryKey val url: String,
    val category: LibraryCategory,
    val prevKey: Int?,
    val nextKey: Int?,
)