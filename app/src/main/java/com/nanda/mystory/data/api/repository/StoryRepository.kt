package com.nanda.mystory.data.api.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.nanda.mystory.data.api.remote.response.DetailResponse
import com.nanda.mystory.data.api.remote.response.ListStoryItem
import com.nanda.mystory.data.api.remote.response.LoginResponse
import com.nanda.mystory.data.api.remote.response.RegisterResponse
import com.nanda.mystory.data.api.remote.response.StoryResponse
import com.nanda.mystory.data.api.remote.response.UploadResponse
import com.nanda.mystory.data.api.remote.retrofit.ApiService
import com.nanda.mystory.data.model.DataModel
import com.nanda.mystory.data.paging.StoryRemoteMediator
import com.nanda.mystory.data.room.StoryDatabase
import com.nanda.mystory.utils.AuthPreference
import com.nanda.mystory.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val database: StoryDatabase,
    private val authPreference: AuthPreference

) {

    fun postRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)

        try {
            val client = apiService.register(name, email, password)
            if (!client.error!!) {
                emit(Result.Success(client))
            } else {
                val errorMessage = client.message ?: "Unknown error"
                Log.e("PostRegister", errorMessage)
                emit(Result.Failure(errorMessage))
            }
        } catch (e: HttpException) {
            val errorMessage = "HTTP error: ${e.message}"
            Log.e("PostRegisterHTTP", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: IOException) {
            val errorMessage = "IO error: ${e.message}"
            Log.e("PostRegisterIO", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            Log.e("PostRegister", errorMessage, e)
            emit(Result.Failure(errorMessage))
        }
    }

    fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)

        try {
            val client = apiService.login(email, password)
            if (!client.error!!) {
                emit(Result.Success(client))
            } else {
                Log.e("PostLogin", client.message ?: "Unknown error")
                emit(Result.Failure(client.message ?: "Unknown error"))
            }
        } catch (e: HttpException) {
            val errorMessage = "HTTP error: ${e.message}"
            Log.e("PostLoginHTTP", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: IOException) {
            val errorMessage = "IO error: ${e.message}"
            Log.e("PostLoginIO", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            Log.e("PostLogin", errorMessage, e)
            emit(Result.Failure(errorMessage))
        }
    }


    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(apiService = apiService, storyDatabase = database),
            pagingSourceFactory = {
                Log.d("PagingSourceFactory", "Fetching stories from the database")
                database.storyDao().getAllStory()
            }
        ).liveData.also {
            it.observeForever { pagingData ->
                Log.d("PagingDataObserver", "New data received: $pagingData")
            }
        }
    }


    fun getDetailStory(id: String): LiveData<Result<DetailResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val client = apiService.getDetailedStory(id)
            if (!client.error!!) {
                emit(Result.Success(client))
            } else {
                emit(Result.Failure(client.message.toString()))
            }
        } catch (e: HttpException) {
            val errorMessage = "HTTP error: ${e.message()}"
            Log.e("DetailStoriesHTTP", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: IOException) {
            val errorMessage = "IO error: ${e.message}"
            Log.e("DetailStoriesIO", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            Log.e("DetailStories", errorMessage, e)
            emit(Result.Failure(errorMessage))
        }
    }



    fun postStory(
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): LiveData<Result<UploadResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)

        try {
            val client = apiService.postStory(multipartBody, description, lat, lon)
            if (!client.error!!) {
                emit(Result.Success(client))
            } else {
                emit(Result.Failure(client.message.toString()))
            }
        } catch (e: HttpException) {
            val errorMessage = "HTTP error: ${e.message}"
            Log.e("PostStoryHTTP", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: IOException) {
            val errorMessage = "IO error: ${e.message}"
            Log.e("PostStoryIO", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            Log.e("PostStory", errorMessage, e)
            emit(Result.Failure(errorMessage))
        }
    }


    fun postStoryGuest(
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): LiveData<Result<UploadResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)

        try {
            val client = apiService.postStoryGuest(multipartBody, description, lat, lon)
            if (!client.error!!) {
                emit(Result.Success(client))
            } else {
                emit(Result.Failure(client.message.toString()))
            }
        } catch (e: HttpException) {
            val errorMessage = "HTTP error: ${e.message}"
            Log.e("PostStoryGuestHTTP", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: IOException) {
            val errorMessage = "IO error: ${e.message}"
            Log.e("PostStoryGuestIO", errorMessage)
            emit(Result.Failure(errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            Log.e("PostStoryGuest", errorMessage, e)
            emit(Result.Failure(errorMessage))
        }
    }


    fun getMapStories(): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val client = apiService.getMapStories(1)
            emit(Result.Success(client))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Unknown error"
            emit(Result.Failure(errorMessage))
            Log.e("GetMapStories", errorMessage, e)
        }
    }


    suspend fun saveData(dataModel: DataModel) = authPreference.saveData(dataModel)

    fun getData(): Flow<DataModel> = authPreference.getData()

    suspend fun logout() = authPreference.logout()



    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            authPreference: AuthPreference,
            database: StoryDatabase
        ): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, database, authPreference).also {
                    instance = it
                }
            }
        }
    }

}