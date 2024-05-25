package com.nanda.mystory.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nanda.mystory.data.api.remote.response.RegisterResponse
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.utils.Result

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun postRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = storyRepository.postRegister(name, email, password)
}