package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.data.remote.model.PersonDetail
import com.vishalpvijayan.themovieapp.di.TmdbApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    @TmdbApi private val apiService: ApiService
) : ViewModel() {

    private val _person = MutableLiveData<PersonDetail?>()
    val person: LiveData<PersonDetail?> = _person

    private val _movies = MutableLiveData<List<Movie>>(emptyList())
    val movies: LiveData<List<Movie>> = _movies

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun load(personId: Int) {
        viewModelScope.launch {
            _loading.postValue(true)
            _error.postValue(null)
            runCatching {
                val personResp = apiService.getPersonDetails(personId)
                val creditsResp = apiService.getPersonMovieCredits(personId)
                _person.postValue(personResp.body())
                _movies.postValue(
                    creditsResp.body()?.cast
                        .orEmpty()
                        .distinctBy { it.id }
                        .sortedByDescending { it.popularity ?: 0.0 }
                )
            }.onFailure {
                _error.postValue("Failed to load person details")
            }
            _loading.postValue(false)
        }
    }
}
