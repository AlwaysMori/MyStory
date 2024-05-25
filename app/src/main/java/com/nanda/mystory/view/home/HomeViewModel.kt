package com.nanda.mystory.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nanda.mystory.data.api.remote.response.ListStoryItem
import com.nanda.mystory.data.api.repository.StoryRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    val stories: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

}