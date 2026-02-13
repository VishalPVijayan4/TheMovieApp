package com.vishalpvijayan.themovieapp.data.remote.datasource

import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.di.TmdbApi
import javax.inject.Inject

class MovieRemoteDataSource  @Inject constructor(
    @TmdbApi private val tmdbApiService: ApiService
) {

    suspend fun fetchMoviesByCategory(category: String): List<Movie>? {
        val response = when (category) {
            "now_playing" -> tmdbApiService.getNowPlayingMovies()
            "popular" -> tmdbApiService.getPopularMovies()
            "top_rated" -> tmdbApiService.getTopRatedMovies()
            "upcoming" -> tmdbApiService.getUpcomingMovies()
            else -> tmdbApiService.getTrendingMovies()
        }
        return if (response.isSuccessful) response.body()?.results else null
    }

/*    suspend fun fetchTrendingMovies(): List<Movie>? {
        return try {
            val response = tmdbApiService.getTrendingMovies()
            if (response.isSuccessful) response.body()?.results else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchMovieDetails(movieId: Int): Movie? {
        return try {
            val response = tmdbApiService.getMovieDetails(movieId)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }*/
}