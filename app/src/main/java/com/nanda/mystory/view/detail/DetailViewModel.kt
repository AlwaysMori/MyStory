package com.nanda.mystory.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.data.api.remote.response.DetailResponse
import com.nanda.mystory.utils.Result

class DetailViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getDetailStory(id: String): LiveData<Result<DetailResponse>> =
        storyRepository.getDetailStory(id)
}