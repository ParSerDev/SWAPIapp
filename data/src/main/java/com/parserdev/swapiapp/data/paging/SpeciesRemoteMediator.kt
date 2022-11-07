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
class SpeciesRemoteMediator(
    private val query: String,
    private val service: LibraryService,
    private val libraryDatabase: LibraryDatabase
) : RemoteMediator<Int, LibraryItem>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, LibraryItem>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_STARTING_PAGE_INDEX

            }
            LoadType.PREPEND -> {

                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val apiResponse = service.searchSpecies(query,page)

            val species = apiResponse.results?.map { it.mapToLibraryItem() }?: listOf()
            val endOfPaginationReached = species.isEmpty()|| page==1 && species.size< NETWORK_PAGE_SIZE
            libraryDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    libraryDatabase.remoteKeysDao().clearLibraryItemsRemoteKeys(category = LibraryCategory.SPECIE)
                    libraryDatabase.libraryDao().clearLibraryItems(category = LibraryCategory.SPECIE)
                }
                val prevKey = if (page == DEFAULT_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = species.map {
                    LibraryItemRemoteKeys(url = it.url, category = LibraryCategory.SPECIE, prevKey = prevKey, nextKey = nextKey)
                }
                libraryDatabase.remoteKeysDao().insertAllLibraryItemsRemoteKeys(keys)
                libraryDatabase.libraryDao().insertAllLibraryItems(species)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, LibraryItem>): LibraryItemRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { specie ->
                // Get the remote keys of the last item retrieved
                libraryDatabase.remoteKeysDao().remoteKeysLibraryItemUrl(url = specie.url)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, LibraryItem>): LibraryItemRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { specie ->
                // Get the remote keys of the first items retrieved
                libraryDatabase.remoteKeysDao().remoteKeysLibraryItemUrl(specie.url)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, LibraryItem>
    ): LibraryItemRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { url ->
                libraryDatabase.remoteKeysDao().remoteKeysLibraryItemUrl(url)
            }
        }
    }
}