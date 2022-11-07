package com.parserdev.swapiapp.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.EMPTY
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Entity(tableName = "starship")
data class StarshipDto(

    @field:SerializedName("max_atmosphering_speed")
    val maxAtmospheringSpeed: String?,

    @field:SerializedName("cargo_capacity")
    val cargoCapacity: String?,

    @field:SerializedName("passengers")
    val passengers: String?,
    @field:SerializedName("edited")
    val edited: String?,

    @field:SerializedName("MGLT")
    val mGLT: String?,

    @field:SerializedName("consumables")
    val consumables: String?,

    @field:SerializedName("created")
    val created: String?,

    @field:SerializedName("length")
    val length: String?,

    @field:SerializedName("starship_class")
    val starshipClass: String?,

    @PrimaryKey
    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("crew")
    val crew: String?,

    @field:SerializedName("manufacturer")
    val manufacturer: String?,

    @field:SerializedName("hyperdrive_rating")
    val hyperdriveRating: String?,

    @field:SerializedName("cost_in_credits")
    val costInCredits: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("model")
    val model: String?
) {
    fun mapToDetailsItem(): DetailsItem {
        return DetailsItem(
            url = url,
            title = name ?: EMPTY,
            text1 = created ?: EMPTY,
            text2 = edited ?: EMPTY,
            text3 = model ?: EMPTY,
            text4 = starshipClass ?: EMPTY,
            text5 = manufacturer ?: EMPTY,
            text6 = costInCredits ?: EMPTY,
            text7 = length ?: EMPTY,
            text8 = crew ?: EMPTY,
            text9 = passengers ?: EMPTY,
            text10 = maxAtmospheringSpeed ?: EMPTY,
            text11 = hyperdriveRating ?: EMPTY,
            text12 = mGLT ?: EMPTY,
            text13 = cargoCapacity ?: EMPTY,
            text14 = consumables ?: EMPTY,
            category = LibraryCategory.STARSHIP
        )
    }

    fun mapToLibraryItem(): LibraryItem {
        return LibraryItem(
            name ?: EMPTY,
            url,
            LibraryCategory.STARSHIP
        )
    }
}
