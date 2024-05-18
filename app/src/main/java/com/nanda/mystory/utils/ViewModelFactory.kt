package com.nanda.mystory.utils

import com.nanda.mystory.view.main.MainViewModel
import com.nanda.mystory.view.home.HomeViewModel
import androidx.lifecycle.ViewModelProvider
import com.nanda.mystory.view.login.LoginViewModel
import com.nanda.mystory.data.api.repository.StoryRepository
import com.nanda.mystory.view.setting.SettingViewModel
import android.content.Context
import com.nanda.mystory.view.story.StoryViewModel
import com.nanda.mystory.view.detail.DetailViewModel
import com.nanda.mystory.view.register.RegisterViewModel
import com.nanda.mystory.di.Injection
import androidx.lifecycle.ViewModel



class ViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECK_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModal class: ${modelClass.name}")
    }



    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}