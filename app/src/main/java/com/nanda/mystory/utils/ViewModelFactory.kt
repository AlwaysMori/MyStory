package com.nanda.mystory.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.di.Injection
import com.nanda.mystory.view.detail.DetailViewModel
import com.nanda.mystory.view.home.HomeViewModel
import com.nanda.mystory.view.login.LoginViewModel
import com.nanda.mystory.view.main.MainViewModel
import com.nanda.mystory.view.map.MapsViewModel
import com.nanda.mystory.view.register.RegisterViewModel
import com.nanda.mystory.view.setting.SettingViewModel
import com.nanda.mystory.view.story.StoryViewModel

class ViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(storyRepository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(storyRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(storyRepository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(storyRepository) as T
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> StoryViewModel(storyRepository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(storyRepository) as T
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(storyRepository) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(storyRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { INSTANCE = it }
        }
    }
}
