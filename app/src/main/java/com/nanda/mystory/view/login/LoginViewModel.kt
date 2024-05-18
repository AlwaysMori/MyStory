package com.nanda.mystory.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.data.api.remote.response.LoginResponse
import com.nanda.mystory.data.model.DataModel
import com.nanda.mystory.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> =
        storyRepository.postLogin(email, password)

    fun saveData(dataModel: DataModel) {
        viewModelScope.launch {
            storyRepository.saveData(dataModel)
        }
    }
}