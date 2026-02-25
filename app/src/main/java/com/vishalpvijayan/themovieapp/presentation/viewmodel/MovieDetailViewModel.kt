package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.CreditPerson
import com.vishalpvijayan.themovieapp.data.remote.model.FavoriteRequest
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.data.remote.model.WatchlistRequest
import com.vishalpvijayan.themovieapp.data.remote.model.VideoItem
import com.vishalpvijayan.themovieapp.di.TmdbApi
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import com.vishalpvijayan.themovieapp.utilis.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @TmdbApi private val tmdbApiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> get() = _movie

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _videos = MutableLiveData<List<VideoItem>>(emptyList())
    val videos: LiveData<List<VideoItem>> = _videos

    private val _similarMovies = MutableLiveData<List<Movie>>(emptyList())
    val similarMovies: LiveData<List<Movie>> = _similarMovies

    private val _imagesInfo = MutableLiveData<String>("")
    val imagesInfo: LiveData<String> = _imagesInfo

    private val _credits = MutableLiveData<List<CreditPerson>>(emptyList())
    val credits: LiveData<List<CreditPerson>> = _credits

    private val _trailerKey = MutableLiveData<String?>(null)
    val trailerKey: LiveData<String?> = _trailerKey

    private val _watchProvidersText = MutableLiveData("Watch providers unavailable")
    val watchProvidersText: LiveData<String> = _watchProvidersText
    private val _watchProvidersLink = MutableLiveData<String?>(null)
    val watchProvidersLink: LiveData<String?> = _watchProvidersLink

    private val _favorite = MutableLiveData(false)
    val favorite: LiveData<Boolean> = _favorite

    private val _watchlist = MutableLiveData(false)
    val watchlist: LiveData<Boolean> = _watchlist

    fun getMovieDetail(movieId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = userRepository.fetchMovieDetails(movieId)
                _movie.value = result
                _error.value = null

                val videosResponse = tmdbApiService.getMovieVideos(movieId)
                val videos = videosResponse.body()?.results.orEmpty()
                _videos.postValue(videos)
                _trailerKey.postValue(videos.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }?.key)

                val creditsResponse = tmdbApiService.getMovieCredits(movieId)
                _credits.postValue(creditsResponse.body()?.cast.orEmpty().take(15))

                val similarResponse = tmdbApiService.getSimilarMovies(movieId)
                _similarMovies.postValue(similarResponse.body()?.results.orEmpty().take(15))

                val providersResponse = tmdbApiService.getMovieWatchProviders(movieId)
                val providers = providersResponse.body()?.results.orEmpty()
                if (providers.isNotEmpty()) {
                    _watchProvidersText.postValue("Where to watch: " + providers.keys.take(6).joinToString())
                    _watchProvidersLink.postValue(providers.values.firstOrNull()?.link)
                }

                val imagesResponse = tmdbApiService.getMovieImages(movieId)
                val posters = imagesResponse.body()?.posters?.size ?: 0
                val backdrops = imagesResponse.body()?.backdrops?.size ?: 0
                _imagesInfo.postValue("Images: $posters posters • $backdrops backdrops")

                loadStates(movieId)
            } catch (e: Exception) {
                _error.value = "Failed to load movie details"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadStates(movieId: Int) {
        val sessionId = sessionManager.getSessionId() ?: return
        runCatching {
            val states = tmdbApiService.getMovieAccountStates(movieId, sessionId).body()
            _favorite.postValue(states?.favorite == true)
            _watchlist.postValue(states?.watchlist == true)
        }
    }

    fun toggleFavorite(movieId: Int) {
        val sessionId = sessionManager.getSessionId() ?: run {
            _error.postValue("Login required to manage favorites")
            return
        }
        val accountId = sessionManager.getAccountId() ?: run {
            _error.postValue("Missing account information, please login again")
            return
        }
        val newValue = !(_favorite.value ?: false)
        viewModelScope.launch {
            runCatching {
                tmdbApiService.setFavorite(
                    accountId = accountId,
                    request = FavoriteRequest(media_type = "movie", media_id = movieId, favorite = newValue),
                    sessionId = sessionId
                )
            }.onSuccess { _favorite.postValue(newValue) }
                .onFailure { _error.postValue("Failed to update favorite") }
        }
    }

    fun toggleWatchlist(movieId: Int) {
        val sessionId = sessionManager.getSessionId() ?: run {
            _error.postValue("Login required to manage watchlist")
            return
        }
        val accountId = sessionManager.getAccountId() ?: run {
            _error.postValue("Missing account information, please login again")
            return
        }
        val newValue = !(_watchlist.value ?: false)
        viewModelScope.launch {
            runCatching {
                tmdbApiService.setWatchlist(
                    accountId = accountId,
                    request = WatchlistRequest(media_type = "movie", media_id = movieId, watchlist = newValue),
                    sessionId = sessionId
                )
            }.onSuccess { _watchlist.postValue(newValue) }
                .onFailure { _error.postValue("Failed to update watchlist") }
        }
    }
}
