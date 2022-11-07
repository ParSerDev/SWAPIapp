package com.parserdev.swapiapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.parserdev.swapiapp.data.db.dao.details.DetailsDao
import com.parserdev.swapiapp.data.db.dao.library.LibraryDao
import com.parserdev.swapiapp.data.db.dao.library.RemoteKeysDao
import com.parserdev.swapiapp.data.dto.library.LibraryItemRemoteKeys
import com.parserdev.swapiapp.data.dto.library.LibraryItem
import com.parserdev.swapiapp.data.dto.details.DetailsItem

@Database(
    entities = [LibraryItem::class,
        LibraryItemRemoteKeys::class,
        DetailsItem::class],
    version = 9,
    exportSchema = false
)
abstract class LibraryDatabase : RoomDatabase() {

    abstract fun libraryDao(): LibraryDao
    abstract fun detailsDao(): DetailsDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getInstance(context: Context): LibraryDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LibraryDatabase::class.java, "Library"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}