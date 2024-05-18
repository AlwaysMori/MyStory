package com.nanda.mystory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.data.model.DataModel
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getLoginData(): LiveData<DataModel> {
        return storyRepository.getData().asLiveData()
    }

    suspend fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }
}