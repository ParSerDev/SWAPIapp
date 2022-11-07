package com.parserdev.swapiapp.data.db.dao.library

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parserdev.swapiapp.data.dto.*
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.domain.entities.LibraryCategory

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLibraryItems(libraryItems: List<LibraryItem>)

    @Query(
        "SELECT * FROM libraryItem WHERE " +
                "title LIKE :queryString AND " +
                "category LIKE :category " +
                "ORDER BY title ASC"
    )
    fun libraryItemsByTitle(
        queryString: String,
        category: LibraryCategory
    ): PagingSource<Int, LibraryItem>

    @Query(
        "DELETE FROM libraryItem WHERE " +
                "category LIKE :category"
    )
    suspend fun clearLibraryItems(category: LibraryCategory)

}
