package com.vishalpvijayan.themovieapp.data.remote.model

data class PersonDetail(
    val id: Int,
    val name: String? = null,
    val biography: String? = null,
    val birthday: String? = null,
    val place_of_birth: String? = null,
    val profile_path: String? = null,
    val known_for_department: String? = null
)

data class PersonCreditsResponse(
    val cast: List<Movie> = emptyList()
)

data class FavoriteRequest(
    val media_type: String,
    val media_id: Int,
    val favorite: Boolean
)


data class WatchlistRequest(
    val media_type: String,
    val media_id: Int,
    val watchlist: Boolean
)
