package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.di.TmdbApi
import com.vishalpvijayan.themovieapp.utilis.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaCollectionViewModel @Inject constructor(
    @TmdbApi private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>(emptyList())
    val movies: LiveData<List<Movie>> = _movies

    private val _tv = MutableLiveData<List<Movie>>(emptyList())
    val tv: LiveData<List<Movie>> = _tv

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun load(type: String) {
        viewModelScope.launch {
            val accountId = sessionManager.getAccountId()
            if (accountId == null) {
                _error.postValue("Please login again")
                return@launch
            }
            _loading.postValue(true)
            _error.postValue(null)
            runCatching {
                if (type == "favorite") {
                    val movieResp = apiService.getFavoriteMovies(accountId)
                    val tvResp = apiService.getFavoriteTv(accountId)
                    _movies.postValue(movieResp.body()?.results.orEmpty())
                    _tv.postValue(tvResp.body()?.results.orEmpty())
                } else {
                    val movieResp = apiService.getWatchlistMovies(accountId)
                    val tvResp = apiService.getWatchlistTv(accountId)
                    _movies.postValue(movieResp.body()?.results.orEmpty())
                    _tv.postValue(tvResp.body()?.results.orEmpty())
                }
            }.onFailure {
                _error.postValue("Failed to load collection")
            }
            _loading.postValue(false)
        }
    }
}
