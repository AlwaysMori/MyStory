package com.nanda.mystory.data.api.remote.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import com.nanda.mystory.data.api.remote.response.DetailResponse
import com.nanda.mystory.data.api.remote.response.LoginResponse
import com.nanda.mystory.data.api.remote.response.RegisterResponse
import com.nanda.mystory.data.api.remote.response.StoryResponse
import com.nanda.mystory.data.api.remote.response.UploadResponse


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailedStory(
        @Path("id") id: String
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): UploadResponse

    @Multipart
    @POST("stories/guest")
    suspend fun postStoryGuest(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): UploadResponse
}