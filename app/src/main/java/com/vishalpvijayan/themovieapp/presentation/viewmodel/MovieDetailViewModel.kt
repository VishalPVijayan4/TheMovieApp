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
class MovieDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _movie = MutableLiveData<Movie?>()
    val movie: LiveData<Movie?> get() = _movie

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun getMovieDetail(movieId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = userRepository.fetchMovieDetails(movieId)
                _movie.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load movie details"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
