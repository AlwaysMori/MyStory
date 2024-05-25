package com.nanda.mystory.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nanda.mystory.data.api.remote.response.StoryResponse
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.utils.Result

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getMapStories(): LiveData<Result<StoryResponse>> = storyRepository.getMapStories()
}