package com.parserdev.swapiapp.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.EMPTY
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Entity(tableName = "film")
data class FilmDto(

    @field:SerializedName("edited")
    val edited: String?,

    @field:SerializedName("created")
    val created: String?,

    @field:SerializedName("director")
    val director: String?,

    @field:SerializedName("opening_crawl")
    val openingCrawl: String?,

    @field:SerializedName("title")
    val title: String?,

    @PrimaryKey
    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("episode_id")
    val episodeId: Int?,

    @field:SerializedName("release_date")
    val releaseDate: String?,

    @field:SerializedName("producer")
    val producer: String?
) {
    fun mapToDetailsItem(): DetailsItem {
        return DetailsItem(
            url = url,
            title = title ?: EMPTY,
            text1 = created ?: EMPTY,
            text2 = edited ?: EMPTY,
            text3 = episodeId.toString(),
            text4 = director ?: EMPTY,
            text5 = producer ?: EMPTY,
            text6 = releaseDate ?: EMPTY,
            text7 = openingCrawl ?: EMPTY,
            category = LibraryCategory.FILM
        )
    }

    fun mapToLibraryItem(): LibraryItem {
        return LibraryItem(
            title ?: EMPTY,
            url,
            LibraryCategory.FILM
        )
    }
}
