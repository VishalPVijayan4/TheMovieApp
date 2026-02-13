package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _trendingMovies = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>> = _trendingMovies

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private var currentCategory: String = "now_playing"

    fun loadMovies(category: String = currentCategory) {
        currentCategory = category
        viewModelScope.launch {
            _loading.postValue(true)
            _error.postValue(null)
            runCatching {
                userRepository.fetchMoviesByCategory(category, page = 1)
            }.onSuccess { movies ->
                _trendingMovies.postValue(movies?.take(30) ?: emptyList())
            }.onFailure {
                _error.postValue("Could not load list. Go for a retry.")
            }
            _loading.postValue(false)
        }
    }

    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val movie = userRepository.fetchMovieDetails(movieId)
            movie?.let { _selectedMovie.postValue(it) }
        }
    }
}
