package com.nanda.mystory.data.api.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null

)
