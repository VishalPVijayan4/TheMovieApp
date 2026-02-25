package com.vishalpvijayan.themovieapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class MultiSearchResponse(
    val results: List<SearchResultItem> = emptyList()
)

data class SearchResultItem(
    val id: Int,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName(value = "title", alternate = ["name"]) val title: String? = null,
    val overview: String? = null,
    @SerializedName(value = "poster_path", alternate = ["profile_path", "backdrop_path"]) val imagePath: String? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("known_for") val knownFor: List<Movie>? = null,
    @SerializedName("known_for_department") val knownForDepartment: String? = null
)
