package com.parserdev.swapiapp.data.repository

import com.parserdev.swapiapp.data.SWLibraryRepository
import com.parserdev.swapiapp.data.models.categories.characters.SWLibraryCategoryCharacters
import com.parserdev.swapiapp.data.storage.local.LocalStorage
import com.parserdev.swapiapp.data.storage.remote.retrofit.RetrofitSWInstance
import com.parserdev.swapiapp.domain.model.SWCharacter
import retrofit2.Response

class SWLibraryRepositoryImpl(
    val retrofitSWInstance: RetrofitSWInstance,
    //val localStorage: LocalStorage
) :
    SWLibraryRepository {

    override suspend fun requestAllCharacters(): Response<SWLibraryCategoryCharacters> {
        return retrofitSWInstance.api.getSWLibraryCharacters(listOf(1, 2, 3))
    }

    override suspend fun getCharacter(url: String): Response<SWCharacter> {
        return retrofitSWInstance.api.getSWCharacter(url = url)
    }

}