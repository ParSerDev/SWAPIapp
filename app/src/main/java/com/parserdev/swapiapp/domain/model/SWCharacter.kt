package com.parserdev.swapiapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SWCharacter(
    val birth_year: String,
    val eye_color: String,
    val gender: String,
    val hair_color: String,
    val height: String,
    val mass: String,
    val name: String,
    val skin_color: String,
    @PrimaryKey(autoGenerate = false)
    val url: String
)