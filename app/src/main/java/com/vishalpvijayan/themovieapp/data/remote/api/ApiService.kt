package com.vishalpvijayan.themovieapp.data.remote.api

import com.vishalpvijayan.themovieapp.data.remote.model.AccountDetails
import com.vishalpvijayan.themovieapp.data.remote.model.AuthTokenResponse
import com.vishalpvijayan.themovieapp.data.remote.model.DeleteSessionRequest
import com.vishalpvijayan.themovieapp.data.remote.model.LoginRequest
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.data.remote.model.EpisodeGroupsResponse
import com.vishalpvijayan.themovieapp.data.remote.model.GenreListResponse
import com.vishalpvijayan.themovieapp.data.remote.model.MovieImagesResponse
import com.vishalpvijayan.themovieapp.data.remote.model.TvSeriesDetail
import com.vishalpvijayan.themovieapp.data.remote.model.VideoResponse
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


    @GET("discover/movie")
    suspend fun discoverMovie(
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") withGenres: Int? = null
    ): Response<MovieResponse>

    @GET("discover/tv")
    suspend fun discoverTv(
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_null_first_air_dates") includeNullFirstAirDates: Boolean = false,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") withGenres: Int? = null
    ): Response<MovieResponse>

    @GET("tv/airing_today")
    suspend fun getAiringTodayTv(@Query("page") page: Int): Response<MovieResponse>

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTv(@Query("page") page: Int): Response<MovieResponse>

    @GET("tv/popular")
    suspend fun getPopularTv(@Query("page") page: Int): Response<MovieResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTv(@Query("page") page: Int): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): Response<MovieResponse>

    @GET("search/tv")
    suspend fun searchTv(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<Movie>


    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") movieId: Int): Response<VideoResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(@Path("movie_id") movieId: Int): Response<MovieImagesResponse>

    @GET("tv/{series_id}")
    suspend fun getTvSeriesDetails(@Path("series_id") seriesId: Int): Response<TvSeriesDetail>

    @GET("tv/{series_id}/videos")
    suspend fun getTvSeriesVideos(@Path("series_id") seriesId: Int): Response<VideoResponse>

    @GET("tv/{series_id}/episode_groups")
    suspend fun getTvEpisodeGroups(@Path("series_id") seriesId: Int): Response<EpisodeGroupsResponse>

    @GET("tv/{series_id}/similar")
    suspend fun getSimilarTvSeries(
        @Path("series_id") seriesId: Int,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): Response<GenreListResponse>

    @GET("genre/tv/list")
    suspend fun getTvGenres(): Response<GenreListResponse>

    @GET("account/{account_id}")
    suspend fun getAccountDetails(@Path("account_id") accountId: Int = 8167978): Response<AccountDetails>
}
