package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.AccountDetails
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.di.TmdbApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SectionState(
    val title: String,
    val category: String,
    val movies: List<Movie> = emptyList(),
    val visibleCount: Int = 10,
    val page: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @TmdbApi private val apiService: ApiService
) : ViewModel() {

    private val categories = listOf(
        "now_playing" to "Now Playing",
        "popular" to "Popular",
        "top_rated" to "Top Rated",
        "upcoming" to "Upcoming"
    )

    private val _sections = MutableLiveData(
        categories.associate { (category, title) -> category to SectionState(title = title, category = category) }
    )
    val sections: LiveData<Map<String, SectionState>> = _sections

    private val _accountDetails = MutableLiveData<AccountDetails?>()
    val accountDetails: LiveData<AccountDetails?> = _accountDetails

    init {
        loadAllSections()
        loadAccount()
    }

    fun loadAllSections() {
        viewModelScope.launch {
            categories.map { (category, _) ->
                async { reloadSection(category) }
            }.awaitAll()
        }
    }

    fun reloadSection(category: String) {
        viewModelScope.launch {
            updateSection(category) { it.copy(isLoading = true, error = null, page = 1) }
            runCatching {
                fetchCategory(category, 1)
            }.onSuccess { movies ->
                updateSection(category) { it.copy(movies = movies, isLoading = false, error = null, page = 1, visibleCount = 10) }
            }.onFailure {
                updateSection(category) { it.copy(isLoading = false, error = "Failed to load. Tap reload.") }
            }
        }
    }

    fun loadMore(category: String) {
        val current = _sections.value?.get(category) ?: return
        val localMore = current.visibleCount + 5
        if (current.movies.size >= localMore) {
            updateSection(category) { it.copy(visibleCount = localMore.coerceAtMost(it.movies.size)) }
            return
        }

        viewModelScope.launch {
            updateSection(category) { it.copy(isLoading = true, error = null) }
            val nextPage = current.page + 1
            runCatching { fetchCategory(category, nextPage) }
                .onSuccess { next ->
                    val merged = (current.movies + next).distinctBy { it.id }
                    updateSection(category) {
                        it.copy(
                            movies = merged,
                            page = nextPage,
                            visibleCount = (it.visibleCount + 5).coerceAtMost(merged.size),
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure {
                    updateSection(category) { it.copy(isLoading = false, error = "Load more failed. Retry.") }
                }
        }
    }

    fun visibleMovies(category: String, state: SectionState): List<Movie> {
        return state.movies.take(state.visibleCount)
    }

    private suspend fun fetchCategory(category: String, page: Int): List<Movie> {
        val response = when (category) {
            "now_playing" -> apiService.getNowPlayingMovies(page)
            "popular" -> apiService.getPopularMovies(page)
            "top_rated" -> apiService.getTopRatedMovies(page)
            "upcoming" -> apiService.getUpcomingMovies(page)
            else -> apiService.getNowPlayingMovies(page)
        }
        return response.body()?.results.orEmpty().take(15)
    }

    private fun loadAccount() {
        viewModelScope.launch {
            runCatching { apiService.getAccountDetails() }
                .onSuccess { _accountDetails.postValue(it.body()) }
        }
    }

    private fun updateSection(category: String, reducer: (SectionState) -> SectionState) {
        val current = _sections.value ?: return
        val target = current[category] ?: return
        _sections.postValue(current + (category to reducer(target)))
    }
}
