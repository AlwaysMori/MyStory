package com.nanda.mystory.data.api.remote.retrofit

import com.nanda.mystory.data.api.remote.response.DetailResponse
import com.nanda.mystory.data.api.remote.response.LoginResponse
import com.nanda.mystory.data.api.remote.response.RegisterResponse
import com.nanda.mystory.data.api.remote.response.StoryResponse
import com.nanda.mystory.data.api.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailedStory(
        @Path("id") id: String
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): UploadResponse

    @Multipart
    @POST("stories/guest")
    suspend fun postStoryGuest(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): UploadResponse

    @GET("stories")
    suspend fun getMapStories(
        @Query("location") location: Int
    ): StoryResponse
}