package com.vishalpvijayan.themovieapp.data.remote.model

data class AccountStatesResponse(
    val id: Int,
    val favorite: Boolean = false,
    val watchlist: Boolean = false
)
