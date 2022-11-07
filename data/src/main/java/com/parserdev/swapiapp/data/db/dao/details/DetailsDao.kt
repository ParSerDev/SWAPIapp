package com.parserdev.swapiapp.data.db.dao.details

import androidx.room.*
import com.parserdev.swapiapp.data.dto.*
import com.parserdev.swapiapp.data.dto.details.DetailsItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailsItem(detailsItem: DetailsItem)

    @Query(
        "SELECT * FROM detailsItem WHERE " +
                "url LIKE :url"
    )
    fun getDetailsItemByUrl(url: String): Flow<DetailsItem>

    @Query(
        "DELETE FROM detailsItem WHERE " +
                "url LIKE :url"
    )
    suspend fun clearDetailsItem(vararg url: String)

    @Query("SELECT EXISTS(SELECT * FROM detailsItem WHERE url = :url)")
    fun isDetailsItemExists(url: String): Boolean
}
