package com.vishalpvijayan.themovieapp.data.remote.api

import com.vishalpvijayan.themovieapp.data.remote.model.AccountDetails
import com.vishalpvijayan.themovieapp.data.remote.model.AuthTokenResponse
import com.vishalpvijayan.themovieapp.data.remote.model.DeleteSessionRequest
import com.vishalpvijayan.themovieapp.data.remote.model.LoginRequest
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.data.remote.model.MovieResponse
import com.vishalpvijayan.themovieapp.data.remote.model.SessionRequest
import com.vishalpvijayan.themovieapp.data.remote.model.SessionResponse
import com.vishalpvijayan.themovieapp.data.remote.model.TmdbStatusResponse
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
    @GET("authentication/token/new")
    suspend fun createRequestToken(): Response<AuthTokenResponse>

    @POST("authentication/token/validate_with_login")
    suspend fun validateWithLogin(@Body request: LoginRequest): Response<AuthTokenResponse>

    @POST("authentication/session/new")
    suspend fun createSession(@Body request: SessionRequest): Response<SessionResponse>

    @retrofit2.http.HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(@Body request: DeleteSessionRequest): Response<TmdbStatusResponse>

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(): Response<MovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<Movie>

    @GET("account/{account_id}")
    suspend fun getAccountDetails(@Path("account_id") accountId: Int = 8167978): Response<AccountDetails>
}