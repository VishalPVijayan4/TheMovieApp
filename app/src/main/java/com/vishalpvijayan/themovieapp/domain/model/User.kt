package com.vishalpvijayan.themovieapp.domain.model


data class User(
    val id: Int = 0,             // Matching userId from Entity
    val fullName: String,
    val position: String,
    val profilePic: String? = null,
    val isSynced: Boolean = false,
)

