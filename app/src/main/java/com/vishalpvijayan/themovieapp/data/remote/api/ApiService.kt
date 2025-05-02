package com.vishalpvijayan.themovieapp.data.remote.api

import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.data.remote.model.MovieResponse
import com.vishalpvijayan.themovieapp.data.remote.model.UserRequest
import com.vishalpvijayan.themovieapp.data.remote.model.UserResponse
import com.vishalpvijayan.themovieapp.data.remote.model.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int): UsersResponse

    @POST("users")
    suspend fun addUser(@Body user: UserRequest): Response<UserResponse>

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<Movie>
}