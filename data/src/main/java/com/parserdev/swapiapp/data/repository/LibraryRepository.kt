package com.parserdev.swapiapp.data.repository

import androidx.paging.PagingData
import com.parserdev.swapiapp.data.repository.sub.*
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.entities.LibraryCategory
import com.parserdev.swapiapp.domain.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LibraryRepository @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val filmsRepository: FilmsRepository,
    private val planetsRepository: PlanetsRepository,
    private val speciesRepository: SpeciesRepository,
    private val starshipsRepository: StarshipsRepository,
    private val vehiclesRepository: VehiclesRepository,
) {
    fun getLibraryItems(query: String, category: LibraryCategory): Flow<PagingData<LibraryItem>> {
        return when (category) {
            LibraryCategory.CHARACTER -> charactersRepository.getLibraryStream(
                query = query,
                category = category
            )
            LibraryCategory.FILM -> filmsRepository.getLibraryStream(
                query = query,
                category = category
            )
            LibraryCategory.PLANET -> planetsRepository.getLibraryStream(
                query = query,
                category = category
            )
            LibraryCategory.SPECIE -> speciesRepository.getLibraryStream(
                query = query,
                category = category
            )
            LibraryCategory.STARSHIP -> starshipsRepository.getLibraryStream(
                query = query,
                category = category
            )
            LibraryCategory.VEHICLE -> vehiclesRepository.getLibraryStream(
                query = query,
                category = category
            )
        }
    }

    fun getDetailsItem(
        url: String,
        libraryCategory: LibraryCategory
    ): Flow<NetworkResult<DetailsItem>> {
        return when (libraryCategory) {
            LibraryCategory.CHARACTER -> charactersRepository.getDetailsItem(url = url)
            LibraryCategory.FILM -> filmsRepository.getDetailsItem(url = url)
            LibraryCategory.PLANET -> planetsRepository.getDetailsItem(url = url)
            LibraryCategory.SPECIE -> speciesRepository.getDetailsItem(url = url)
            LibraryCategory.STARSHIP -> starshipsRepository.getDetailsItem(url = url)
            LibraryCategory.VEHICLE -> vehiclesRepository.getDetailsItem(url = url)
        }
    }

}