package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Genre
import com.vishalpvijayan.themovieapp.di.TmdbApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(
    @TmdbApi private val apiService: ApiService
) : ViewModel() {

    private val _movieGenres = MutableLiveData<List<Genre>>(emptyList())
    val movieGenres: LiveData<List<Genre>> = _movieGenres

    private val _tvGenres = MutableLiveData<List<Genre>>(emptyList())
    val tvGenres: LiveData<List<Genre>> = _tvGenres

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun load() {
        viewModelScope.launch {
            _loading.postValue(true)
            runCatching { apiService.getMovieGenres() }.onSuccess { _movieGenres.postValue(it.body()?.genres.orEmpty()) }
            runCatching { apiService.getTvGenres() }.onSuccess { _tvGenres.postValue(it.body()?.genres.orEmpty()) }
            _loading.postValue(false)
        }
    }
}
