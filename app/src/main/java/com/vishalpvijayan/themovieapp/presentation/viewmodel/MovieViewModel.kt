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

    private var currentCategory: String = "now_playing"
    private var page: Int = 1

    fun loadMovies(category: String) {
        currentCategory = category
        page = 1
        viewModelScope.launch {
            val movies = userRepository.fetchMoviesByCategory(category, page = 1)
            _trendingMovies.postValue(movies?.take(15) ?: emptyList())
        }
    }

    fun loadMoreMovies() {
        viewModelScope.launch {
            page += 1
            val more = userRepository.fetchMoviesByCategory(currentCategory, page = page)
            val merged = (_trendingMovies.value.orEmpty() + (more?.take(15) ?: emptyList())).distinctBy { it.id }
            _trendingMovies.postValue(merged)
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
