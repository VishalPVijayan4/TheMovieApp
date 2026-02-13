package com.vishalpvijayan.themovieapp.data.remote.model

data class VideoResponse(
    val results: List<VideoItem> = emptyList()
)

data class VideoItem(
    val key: String? = null,
    val name: String? = null,
    val site: String? = null,
    val type: String? = null
)

data class MovieImagesResponse(
    val backdrops: List<ImageItem> = emptyList(),
    val posters: List<ImageItem> = emptyList()
)

data class ImageItem(
    val file_path: String? = null,
    val width: Int? = null,
    val height: Int? = null
)

data class EpisodeGroupsResponse(
    val results: List<EpisodeGroupItem> = emptyList()
)

data class EpisodeGroupItem(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val episode_count: Int? = null,
    val group_count: Int? = null
)

data class TvSeriesDetail(
    val id: Int,
    val name: String? = null,
    val overview: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val vote_average: Double? = null,
    val first_air_date: String? = null,
    val last_air_date: String? = null,
    val number_of_seasons: Int? = null,
    val number_of_episodes: Int? = null,
    val status: String? = null,
    val genres: List<Genre>? = null
)
