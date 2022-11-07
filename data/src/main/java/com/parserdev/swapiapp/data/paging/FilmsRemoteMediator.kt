package com.parserdev.swapiapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.parserdev.swapiapp.data.db.LibraryDatabase
import com.parserdev.swapiapp.data.dto.library.LibraryItemRemoteKeys
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.data.network.retrofit.LibraryService
import com.parserdev.swapiapp.domain.DEFAULT_STARTING_PAGE_INDEX
import com.parserdev.swapiapp.domain.NETWORK_PAGE_SIZE
import com.parserdev.swapiapp.domain.entities.LibraryCategory
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class FilmsRemoteMediator(
    private val query: String,
    private val service: LibraryService,
    private val libraryDatabase: LibraryDatabase
) : RemoteMediator<Int, LibraryItem>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LibraryItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_STARTING_PAGE_INDEX

            }
            LoadType.PREPEND -> {

                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val apiResponse = service.searchFilms(query, page)

            val films = apiResponse.results.map { it.mapToLibraryItem() }
            val endOfPaginationReached =
                films.isEmpty() || page == 1 && films.size < NETWORK_PAGE_SIZE
            libraryDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    libraryDatabase.remoteKeysDao()
                        .clearLibraryItemsRemoteKeys(category = LibraryCategory.FILM)
                    libraryDatabase.libraryDao().clearLibraryItems(category = LibraryCategory.FILM)
                }

                val prevKey = if (page == DEFAULT_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = films.map {
                    LibraryItemRemoteKeys(
                        url = it.url,
                        category = LibraryCategory.FILM,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                libraryDatabase.remoteKeysDao().insertAllLibraryItemsRemoteKeys(keys)
                libraryDatabase.libraryDao().insertAllLibraryItems(films)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, LibraryItem>): LibraryItemRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { film ->
                libraryDatabase.remoteKeysDao().remoteKeysLibraryItemUrl(url = film.url)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, LibraryItem>): LibraryItemRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { film ->
                libraryDatabase.remoteKeysDao().remoteKeysLibraryItemUrl(film.url)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, LibraryItem>
    ): LibraryItemRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { url ->
                libraryDatabase.remoteKeysDao().remoteKeysLibraryItemUrl(url)
            }
        }
    }
}