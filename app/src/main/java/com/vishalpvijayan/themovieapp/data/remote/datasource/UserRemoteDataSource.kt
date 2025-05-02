package com.vishalpvijayan.themovieapp.data.remote.datasource

import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.data.remote.model.UserRequest
import com.vishalpvijayan.themovieapp.data.remote.model.UsersResponse
import com.vishalpvijayan.themovieapp.di.ReqresApi
import com.vishalpvijayan.themovieapp.di.TmdbApi
import javax.inject.Inject
import javax.inject.Named

class UserRemoteDataSource @Inject constructor(
    @ReqresApi private val reqresApiService: ApiService,
    @TmdbApi private val tmdbApiService: ApiService
) {

    suspend fun getUsers(page: Int): UsersResponse {
        // Make the network call and return the result
        return reqresApiService.getUsers(page)
    }

    suspend fun addUser(request: UserRequest) = reqresApiService.addUser(request)


    suspend fun fetchTrendingMovies(): List<Movie>? {
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
    }

}


