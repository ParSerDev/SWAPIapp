package com.parserdev.swapiapp.data.storage.remote.retrofit.api

import com.parserdev.swapiapp.data.models.categories.characters.SWLibraryCategoryCharacters
import com.parserdev.swapiapp.domain.model.SWCharacter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SWAPI {

    @GET("/api/people")
    suspend fun getSWLibraryCharacters(
        @Query("page") page: List<Int>
    ): Response<SWLibraryCategoryCharacters>

    @GET("{url}")
    suspend fun getSWCharacter(
        @Path("url", encoded = true) url: String
    ): Response<SWCharacter>

}