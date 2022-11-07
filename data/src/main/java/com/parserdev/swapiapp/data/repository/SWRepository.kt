package com.parserdev.swapiapp.data.repository

import androidx.paging.PagingData
import com.parserdev.swapiapp.data.db.LibraryDatabase
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.entities.LibraryCategory
import com.parserdev.swapiapp.domain.network.NetworkResult
import kotlinx.coroutines.flow.Flow

abstract class SWRepository(database: LibraryDatabase) {
    val libraryDao = database.libraryDao()
    val detailsDao = database.detailsDao()
    abstract fun getLibraryStream(
        query: String,
        category: LibraryCategory
    ): Flow<PagingData<LibraryItem>>

    abstract fun getDetailsItem(url: String): Flow<NetworkResult<DetailsItem>>
}
