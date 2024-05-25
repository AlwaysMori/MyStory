package com.nanda.mystory.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nanda.mystory.data.api.remote.response.ListStoryItem
import com.nanda.mystory.data.entity.RemoteKeys
import com.nanda.mystory.data.entity.RemoteKeysDao

@Database(entities = [ListStoryItem::class, RemoteKeys::class], version = 1)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,
                    "story_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}
