package com.parserdev.swapiapp.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.EMPTY
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Entity(tableName = "specie")
data class SpecieDto(

    @field:SerializedName("homeworld")
    val homeworld: String?,

    @field:SerializedName("skin_colors")
    val skinColors: String?,

    @field:SerializedName("edited")
    val edited: String?,

    @field:SerializedName("created")
    val created: String?,

    @field:SerializedName("eye_colors")
    val eyeColors: String?,

    @field:SerializedName("language")
    val language: String?,

    @field:SerializedName("classification")
    val classification: String?,

    @PrimaryKey
    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("hair_colors")
    val hairColors: String?,

    @field:SerializedName("average_height")
    val averageHeight: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("designation")
    val designation: String?,

    @field:SerializedName("average_lifespan")
    val averageLifespan: String?
) {
    fun mapToDetailsItem(): DetailsItem {
        return DetailsItem(
            url = url,
            title = name ?: EMPTY,
            text1 = created ?: EMPTY,
            text2 = edited ?: EMPTY,
            text3 = classification ?: EMPTY,
            text4 = designation ?: EMPTY,
            text5 = averageHeight ?: EMPTY,
            text6 = averageLifespan ?: EMPTY,
            text7 = hairColors ?: EMPTY,
            text8 = eyeColors ?: EMPTY,
            text9 = skinColors ?: EMPTY,
            text10 = language ?: EMPTY,
            category = LibraryCategory.SPECIE
        )
    }

    fun mapToLibraryItem(): LibraryItem {
        return LibraryItem(
            name ?: EMPTY,
            url,
            LibraryCategory.SPECIE
        )
    }
}
