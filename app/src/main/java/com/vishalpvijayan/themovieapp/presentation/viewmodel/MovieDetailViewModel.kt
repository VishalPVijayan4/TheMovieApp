package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.data.remote.model.VideoItem
import com.vishalpvijayan.themovieapp.di.TmdbApi
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @TmdbApi private val tmdbApiService: ApiService
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

    private val _watchProvidersText = MutableLiveData("Watch providers unavailable")
    val watchProvidersText: LiveData<String> = _watchProvidersText
    private val _watchProvidersLink = MutableLiveData<String?>(null)
    val watchProvidersLink: LiveData<String?> = _watchProvidersLink

    fun getMovieDetail(movieId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = userRepository.fetchMovieDetails(movieId)
                _movie.value = result
                _error.value = null

                val videosResponse = tmdbApiService.getMovieVideos(movieId)
                _videos.postValue(videosResponse.body()?.results.orEmpty())

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
            } catch (e: Exception) {
                _error.value = "Failed to load movie details"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
