package com.parserdev.swapiapp.data.db.dao.library

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parserdev.swapiapp.data.dto.library.LibraryItemRemoteKeys
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLibraryItemsRemoteKeys(remoteKey: List<LibraryItemRemoteKeys>)

    @Query("SELECT * FROM library_item_remote_keys WHERE url = :url")
    suspend fun remoteKeysLibraryItemUrl(
        url: String
    ): LibraryItemRemoteKeys?

    @Query("DELETE FROM library_item_remote_keys WHERE category LIKE :category")
    suspend fun clearLibraryItemsRemoteKeys(category: LibraryCategory)

}