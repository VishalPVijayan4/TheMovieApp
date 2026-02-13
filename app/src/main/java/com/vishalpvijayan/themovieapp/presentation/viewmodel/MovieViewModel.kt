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

    fun loadMovies(category: String) {
        viewModelScope.launch {
            val movies = userRepository.fetchMoviesByCategory(category)
            _trendingMovies.postValue(movies ?: emptyList())
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
