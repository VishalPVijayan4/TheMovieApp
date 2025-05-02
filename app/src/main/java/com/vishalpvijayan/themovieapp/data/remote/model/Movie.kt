package com.vishalpvijayan.themovieapp.data.remote.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val vote_average: Double
)
