package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.AccountDetails
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.di.TmdbApi
import com.vishalpvijayan.themovieapp.utilis.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @TmdbApi private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profile = MutableLiveData<AccountDetails?>()
    val profile: LiveData<AccountDetails?> = _profile

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _watchlistMovies = MutableLiveData<List<Movie>>(emptyList())
    val watchlistMovies: LiveData<List<Movie>> = _watchlistMovies

    private val _watchlistTv = MutableLiveData<List<Movie>>(emptyList())
    val watchlistTv: LiveData<List<Movie>> = _watchlistTv

    fun loadProfile() {
        viewModelScope.launch {
            _loading.postValue(true)
            val accountId = sessionManager.getAccountId()
            if (accountId != null) {
                runCatching { apiService.getAccountDetails(accountId) }
                    .onSuccess { _profile.postValue(it.body()) }
            }
            _loading.postValue(false)
        }
    }
}
