package com.nanda.mystory.data.api.remote.retrofit

import android.util.Log
import com.nanda.mystory.utils.AuthPreference
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.nanda.mystory.BuildConfig


object ApiConfig {

    fun getApiService(authPreference: AuthPreference): ApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val authInterceptor = Interceptor {
            val request = it.request()
            val token = runBlocking {
                val tokenValue = authPreference.getData().firstOrNull()?.token ?: ""
                Log.i("TOKENINAPI", tokenValue)
                tokenValue
            }
            val header = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            it.proceed(header)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}