package com.vishalpvijayan.themovieapp.data.remote.datasource

import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.di.TmdbApi
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    @TmdbApi private val tmdbApiService: ApiService
) {

    suspend fun fetchMoviesByCategory(category: String, page: Int = 1): List<Movie>? {
        val response = when (category) {
            "now_playing" -> tmdbApiService.getNowPlayingMovies(page)
            "popular" -> tmdbApiService.getPopularMovies(page)
            "top_rated" -> tmdbApiService.getTopRatedMovies(page)
            "upcoming" -> tmdbApiService.getUpcomingMovies(page)
            else -> tmdbApiService.getTrendingMovies()
        }
        return if (response.isSuccessful) response.body()?.results else null
    }
}
