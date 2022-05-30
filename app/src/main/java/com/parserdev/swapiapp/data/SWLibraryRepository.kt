package com.parserdev.swapiapp.data

import com.parserdev.swapiapp.data.models.categories.characters.SWLibraryCategoryCharacters
import com.parserdev.swapiapp.domain.model.SWCharacter
import retrofit2.Response

interface SWLibraryRepository {
    suspend fun requestAllCharacters(): Response<SWLibraryCategoryCharacters>
    suspend fun getCharacter(url: String): Response<SWCharacter>
}