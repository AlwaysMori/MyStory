package com.nanda.mystory.data.api.repository

import com.nanda.mystory.data.api.remote.response.UploadResponse
import com.nanda.mystory.data.model.DataModel
import okhttp3.RequestBody
import com.nanda.mystory.data.api.remote.response.DetailResponse
import com.nanda.mystory.data.api.remote.response.StoryResponse
import com.nanda.mystory.utils.AuthPreference
import com.nanda.mystory.data.api.remote.retrofit.ApiService
import com.nanda.mystory.utils.Result
import okhttp3.MultipartBody
import androidx.lifecycle.LiveData
import com.nanda.mystory.data.api.remote.response.RegisterResponse
import androidx.lifecycle.liveData
import retrofit2.HttpException
import com.nanda.mystory.data.api.remote.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import android.util.Log

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val authPreference: AuthPreference
) {

    fun postRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)

        try {
            val client = apiService.register(name, email, password)
            if (client.error == false) {
                emit(Result.Success(client))
            } else {
                Log.e("PostRegister", "${client.message}")
            }
        } catch (e: Exception) {
            emit(Result.Failure(e.message.toString()))
            Log.e("PostRegisterHttp", e.message.toString())
        }
    }

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)

        try {
            val client = apiService.login(email, password)
            if (client.error == false) {
                emit(Result.Success(client))
            } else {
                Log.e("PostLogin", "${client.message}")
                emit(Result.Failure(client.message.toString()))
            }
        } catch (e: HttpException) {
            Log.e("PostLoginHTTP", "${e.message}")
            emit(Result.Failure(e.message.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getStories(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)

        try {
            val client = apiService.getStories()
            if (client.error == false) {
                emit(Result.Success(client))
            } else {
                Log.e("GetStories", "${client.message}")
                emit(Result.Failure(client.message.toString()))
            }
        } catch (e: HttpException) {
            Log.e("GetStoriesHTTP", "${e.message}")
            emit(Result.Failure(e.message.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDetailStory(id: String): LiveData<Result<DetailResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getDetailedStory(id)
            if (client.error == false) {
                emit(Result.Success(client))
            } else {
                emit(Result.Failure(client.message.toString()))
            }

        } catch (e: HttpException) {
            Log.e("DetailStoriesHTTP", "${e.message}")
            emit(Result.Failure(e.message.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun postStory(
        multipartBody: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<UploadResponse>> = liveData {
        emit(Result.Loading)


        try {
            val client = apiService.postStory(multipartBody, description)
            if (client.error == false) {
                emit(Result.Success(client))
            } else {
                emit(Result.Failure(client.message.toString()))
            }
        } catch (e: HttpException) {
            Log.e("PostStoryHTTP", "${e.message}")
            emit(Result.Failure(e.message.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun postStoryGuest(
        multipartBody: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<UploadResponse>> = liveData {
        emit(Result.Loading)

        try {
            val client = apiService.postStoryGuest(multipartBody, description)
            if (client.error == false) {
                emit(Result.Success(client))
            } else {
                emit(Result.Failure(client.message.toString()))
            }
        } catch (e: HttpException) {
            Log.e("PostStoryGuestHTTP", "${e.message}")
            emit(Result.Failure(e.message.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveData(dataModel: DataModel) {
        authPreference.saveData(dataModel)
    }

    fun getData(): Flow<DataModel> {
        return authPreference.getData()
    }

    suspend fun logout() {
        authPreference.logout()
    }


    companion object {
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, authPreference: AuthPreference): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, authPreference)
            }.also { instance = it }
    }
}