package com.nanda.mystory.data.entity

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity("remote_keys")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
