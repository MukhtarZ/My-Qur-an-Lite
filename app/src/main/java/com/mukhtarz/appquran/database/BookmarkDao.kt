package com.mukhtarz.appquran.database

import androidx.room.Dao
import androidx.room.DatabaseView
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Insert
    suspend fun insertBookmark(bookmark: Bookmark)

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("DELETE from bookmark")
    suspend fun deleteAllFromBookmark()

//    @Query("SELECT COUNT(*) from bookmark where ayahNumber= ayahNumber")
//    suspend fun checkIfExist(ayahNumber: Int): List<Int>
}