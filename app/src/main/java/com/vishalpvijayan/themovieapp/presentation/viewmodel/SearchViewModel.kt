package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.di.TmdbApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @TmdbApi private val apiService: ApiService
) : ViewModel() {

    private val _results = MutableLiveData<List<Movie>>(emptyList())
    val results: LiveData<List<Movie>> = _results

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun search(query: String, searchTv: Boolean) {
        if (query.isBlank()) {
            _results.value = emptyList()
            _error.value = null
            return
        }
        viewModelScope.launch {
            _loading.postValue(true)
            _error.postValue(null)
            runCatching {
                if (searchTv) apiService.searchTv(query = query) else apiService.searchMovies(query = query)
            }.onSuccess { response ->
                _results.postValue(response.body()?.results.orEmpty())
            }.onFailure {
                _error.postValue("Search failed")
            }
            _loading.postValue(false)
        }
    }
}
