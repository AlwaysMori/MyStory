package com.nanda.mystory.di

import android.content.Context
import com.nanda.mystory.data.api.remote.retrofit.ApiConfig
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.data.room.StoryDatabase
import com.nanda.mystory.utils.AuthPreference
import com.nanda.mystory.utils.dataStore

object Injection {

    fun provideRepository(context: Context): StoryRepository {
        val authPreference = AuthPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(authPreference)
        val database = StoryDatabase.getInstance(context)
        return StoryRepository.getInstance(apiService, authPreference, database)
    }

}