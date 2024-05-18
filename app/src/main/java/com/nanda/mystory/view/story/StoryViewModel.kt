package com.nanda.mystory.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.data.api.remote.response.UploadResponse
import com.nanda.mystory.data.model.DataModel
import com.nanda.mystory.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun postStory(imagePart: MultipartBody.Part, description: RequestBody): LiveData<Result<UploadResponse>> =
        storyRepository.postStory(imagePart, description)

    fun getLoginData(): LiveData<DataModel> =
        storyRepository.getData().asLiveData()

    fun postStoryGuest(imagePart: MultipartBody.Part, description: RequestBody): LiveData<Result<UploadResponse>> =
        storyRepository.postStoryGuest(imagePart, description)
}
