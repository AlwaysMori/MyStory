package com.nanda.mystory.data.api.remote.response

import com.google.gson.annotations.SerializedName

data class Story(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("lat")
    val lat: Float? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("lon")
    val lon: Float? = null,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null
)

data class DetailResponse(

    @field:SerializedName("story")
    val story: Story? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null
)