package com.parserdev.swapiapp.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.EMPTY
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Entity(tableName = "character")
data class CharacterDto(

    @field:SerializedName("homeworld")
    val homeworld: String?,

    @field:SerializedName("gender")
    val gender: String?,

    @field:SerializedName("skin_color")
    val skinColor: String?,

    @field:SerializedName("edited")
    val edited: String?,

    @field:SerializedName("created")
    val created: String?,

    @field:SerializedName("mass")
    val mass: String?,

    @PrimaryKey
    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("hair_color")
    val hairColor: String?,

    @field:SerializedName("birth_year")
    val birthYear: String?,

    @field:SerializedName("eye_color")
    val eyeColor: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("height")
    val height: String?

) {
    fun mapToDetailsItem(): DetailsItem {
        return DetailsItem(
            url = url,
            title = name ?: EMPTY,
            text1 = created ?: EMPTY,
            text2 = edited ?: EMPTY,
            text3 = gender ?: EMPTY,
            text4 = birthYear ?: EMPTY,
            text5 = height ?: EMPTY,
            text6 = mass ?: EMPTY,
            text7 = hairColor ?: EMPTY,
            text8 = eyeColor ?: EMPTY,
            text9 = skinColor ?: EMPTY,
            category = LibraryCategory.CHARACTER
        )
    }

    fun mapToLibraryItem(): LibraryItem {
        return LibraryItem(
            name ?: EMPTY,
            url,
            LibraryCategory.CHARACTER
        )
    }
}
