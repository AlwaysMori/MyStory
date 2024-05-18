package com.nanda.mystory.utils

sealed class Result<out R> private constructor() {
    object Loading : Result<Nothing>()
    data class Failure(val error: String) : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()


}