package com.parserdev.swapiapp.data.storage.local.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["characterName", "filmName"])
class CharacterFilmCrossRef(
    val characterName: String,
    val filmName: String
)