package com.parserdev.swapiapp.data.repository.sub

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.parserdev.swapiapp.data.paging.CharactersRemoteMediator
import com.parserdev.swapiapp.data.db.LibraryDatabase
import com.parserdev.swapiapp.data.dto.*
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.data.network.NetworkInstance
import com.parserdev.swapiapp.data.repository.SWRepository
import com.parserdev.swapiapp.domain.NETWORK_PAGE_SIZE
import com.parserdev.swapiapp.data.utils.networkBoundResource
import com.parserdev.swapiapp.domain.entities.LibraryCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharactersRepository @Inject constructor(
    private val networkInstance: NetworkInstance,
    private val database: LibraryDatabase
) : SWRepository(database = database) {
    override fun getLibraryStream(
        query: String,
        category: LibraryCategory
    ): Flow<PagingData<LibraryItem>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory =
            { libraryDao.libraryItemsByTitle(queryString = dbQuery, category = category) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = CharactersRemoteMediator(
                query,
                networkInstance.api,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun getDetailsItem(url: String) = networkBoundResource(
        query = {
            detailsDao.getDetailsItemByUrl(url)
        },
        fetch = {
            networkInstance.api.fetchCharacter(url)
        },
        saveFetchResult = { item ->
            database.withTransaction {
                detailsDao.clearDetailsItem(url)
                detailsDao.insertDetailsItem(item.mapToDetailsItem())
            }
        }
    )
}