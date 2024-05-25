package com.nanda.mystory.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nanda.mystory.data.api.remote.response.ListStoryItem
@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(story: List<ListStoryItem>)


    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>


    @Query("DELETE FROM story")
    suspend fun deleteAll()
}
