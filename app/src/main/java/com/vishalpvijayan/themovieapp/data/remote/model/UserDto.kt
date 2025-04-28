package com.vishalpvijayan.themovieapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val avatar: String
)
