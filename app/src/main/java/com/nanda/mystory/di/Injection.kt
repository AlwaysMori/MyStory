package com.nanda.mystory.di

import com.nanda.mystory.utils.AuthPreference
import com.nanda.mystory.utils.dataStore
import com.nanda.mystory.data.api.remote.retrofit.ApiConfig
import android.content.Context
import com.nanda.mystory.data.api.repository.StoryRepository


object Injection {

    fun provideRepository(context: Context): StoryRepository {
        val authPref = AuthPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(authPref)
        return StoryRepository.getInstance(apiService, authPref)
    }
}
