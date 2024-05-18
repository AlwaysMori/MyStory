package com.nanda.mystory.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.data.api.remote.response.StoryResponse
import com.nanda.mystory.data.model.DataModel
import com.nanda.mystory.utils.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories(): LiveData<Result<StoryResponse>> = storyRepository.getStories()

    fun getLoginData(): LiveData<DataModel> {
        return storyRepository.getData().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

}