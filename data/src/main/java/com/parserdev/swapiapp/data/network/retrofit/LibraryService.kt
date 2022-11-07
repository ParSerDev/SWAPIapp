package com.parserdev.swapiapp.data.network.retrofit

import com.parserdev.swapiapp.data.dto.*
import com.parserdev.swapiapp.data.dto.PageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LibraryService {
    @GET("people")
    suspend fun searchCharacters(
        @Query("search") query: String,
        @Query("page") page: Int
    ): PageDto<CharacterDto>

    @GET("films")
    suspend fun searchFilms(
        @Query("search") query: String,
        @Query("page") page: Int
    ): PageDto<FilmDto>

    @GET("planets")
    suspend fun searchPlanets(
        @Query("search") query: String,
        @Query("page") page: Int
    ): PageDto<PlanetDto>

    @GET("species")
    suspend fun searchSpecies(
        @Query("search") query: String,
        @Query("page") page: Int
    ): PageDto<SpecieDto>

    @GET("starships")
    suspend fun searchStarships(
        @Query("search") query: String,
        @Query("page") page: Int
    ): PageDto<StarshipDto>

    @GET("vehicles")
    suspend fun searchVehicles(
        @Query("search") query: String,
        @Query("page") page: Int
    ): PageDto<VehicleDto>

    @GET("{url}")
    suspend fun fetchCharacter(
        @Path(value = "url", encoded = true) url: String
    ): CharacterDto

    @GET("{url}")
    suspend fun fetchFilm(
        @Path(value = "url", encoded = true) url: String
    ): FilmDto

    @GET("{url}")
    suspend fun fetchPlanet(
        @Path(value = "url", encoded = true) url: String
    ): PlanetDto

    @GET("{url}")
    suspend fun fetchSpecie(
        @Path(value = "url", encoded = true) url: String
    ): SpecieDto

    @GET("{url}")
    suspend fun fetchStarship(
        @Path(value = "url", encoded = true) url: String
    ): StarshipDto

    @GET("{url}")
    suspend fun fetchVehicle(
        @Path(value = "url", encoded = true) url: String
    ): VehicleDto

}