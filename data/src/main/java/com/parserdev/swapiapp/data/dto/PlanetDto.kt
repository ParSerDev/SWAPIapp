package com.parserdev.swapiapp.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.EMPTY
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Entity(tableName = "planet")
data class PlanetDto(

    @field:SerializedName("edited")
    val edited: String?,

    @field:SerializedName("created")
    val created: String?,

    @field:SerializedName("climate")
    val climate: String?,

    @field:SerializedName("rotation_period")
    val rotationPeriod: String?,

    @PrimaryKey
    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("population")
    val population: String?,

    @field:SerializedName("orbital_period")
    val orbitalPeriod: String?,

    @field:SerializedName("surface_water")
    val surfaceWater: String?,

    @field:SerializedName("diameter")
    val diameter: String?,

    @field:SerializedName("gravity")
    val gravity: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("terrain")
    val terrain: String?
) {
    fun mapToDetailsItem(): DetailsItem {
        return DetailsItem(
            url = url,
            title = name ?: EMPTY,
            text1 = created ?: EMPTY,
            text2 = edited ?: EMPTY,
            text3 = diameter ?: EMPTY,
            text4 = rotationPeriod ?: EMPTY,
            text5 = orbitalPeriod ?: EMPTY,
            text6 = gravity ?: EMPTY,
            text7 = population ?: EMPTY,
            text8 = climate ?: EMPTY,
            text9 = terrain ?: EMPTY,
            text10 = surfaceWater ?: EMPTY,
            category = LibraryCategory.PLANET
        )
    }

    fun mapToLibraryItem(): LibraryItem {
        return LibraryItem(
            name ?: EMPTY,
            url,
            LibraryCategory.PLANET
        )
    }
}

