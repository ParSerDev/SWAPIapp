package com.parserdev.swapiapp.data.dto.details

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Entity(tableName = "detailsItem")
data class DetailsItem(
    @PrimaryKey
    val url: String,
    val title: String,
    val text1: String? = null,
    val text2: String? = null,
    val text3: String? = null,
    val text4: String? = null,
    val text5: String? = null,
    val text6: String? = null,
    val text7: String? = null,
    val text8: String? = null,
    val text9: String? = null,
    val text10: String? = null,
    val text11: String? = null,
    val text12: String? = null,
    val text13: String? = null,
    val text14: String? = null,
    val category: LibraryCategory
)