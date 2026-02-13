package com.vishalpvijayan.themovieapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    @SerializedName(value = "title", alternate = ["name"]) val title: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val vote_average: Double? = null,
    @SerializedName(value = "release_date", alternate = ["first_air_date"]) val release_date: String? = null,
    val original_language: String? = null,
    val popularity: Double? = null,
    val vote_count: Int? = null,
    val adult: Boolean? = null,
    val runtime: Int? = null,
    val status: String? = null,
    val tagline: String? = null,
    val genres: List<Genre>? = null
)

data class Genre(
    val id: Int,
    val name: String
)
