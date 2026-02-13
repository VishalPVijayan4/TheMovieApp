package com.vishalpvijayan.themovieapp.data.remote.datasource

import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.di.TmdbApi
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    @TmdbApi private val tmdbApiService: ApiService
) {

    suspend fun fetchMoviesByCategory(category: String, page: Int = 1): List<Movie>? {
        val response = when {
            category.startsWith("genre_movie_") -> {
                val genreId = category.removePrefix("genre_movie_").toIntOrNull()
                tmdbApiService.discoverMovie(page = page, withGenres = genreId)
            }
            category.startsWith("genre_tv_") -> {
                val genreId = category.removePrefix("genre_tv_").toIntOrNull()
                tmdbApiService.discoverTv(page = page, withGenres = genreId)
            }
            category == "now_playing" -> tmdbApiService.getNowPlayingMovies(page)
            category == "popular" -> tmdbApiService.getPopularMovies(page)
            category == "top_rated" -> tmdbApiService.getTopRatedMovies(page)
            category == "upcoming" -> tmdbApiService.getUpcomingMovies(page)
            category == "tv_discover" -> tmdbApiService.discoverTv(page)
            category == "tv_airing_today" -> tmdbApiService.getAiringTodayTv(page)
            category == "tv_on_the_air" -> tmdbApiService.getOnTheAirTv(page)
            category == "tv_popular" -> tmdbApiService.getPopularTv(page)
            category == "tv_top_rated" -> tmdbApiService.getTopRatedTv(page)
            else -> tmdbApiService.getTrendingMovies()
        }
        return if (response.isSuccessful) response.body()?.results else null
    }
}
