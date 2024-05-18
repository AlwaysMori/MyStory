package com.nanda.mystory.view.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.data.model.DataModel
import kotlinx.coroutines.launch

class SettingViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    fun getData(): LiveData<DataModel> {
        return storyRepository.getData().asLiveData()
    }
}