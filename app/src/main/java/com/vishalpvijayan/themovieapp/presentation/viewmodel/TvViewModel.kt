package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.di.TmdbApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(
    @TmdbApi private val apiService: ApiService
) : ViewModel() {

    private val categories = listOf(
        "tv_discover" to "Discover TV",
        "tv_airing_today" to "Airing Today",
        "tv_on_the_air" to "On The Air",
        "tv_popular" to "Popular TV",
        "tv_top_rated" to "Top Rated TV"
    )

    private val _sections = MutableLiveData(
        categories.associate { (category, title) -> category to SectionState(title = title, category = category) }
    )
    val sections: LiveData<Map<String, SectionState>> = _sections

    init {
        loadAllSections()
    }

    fun loadAllSections() {
        viewModelScope.launch {
            categories.map { (category, _) -> async { reloadSection(category) } }.awaitAll()
        }
    }

    fun reloadSection(category: String) {
        viewModelScope.launch {
            updateSection(category) { it.copy(isLoading = true, error = null, page = 1) }
            runCatching { fetchCategory(category, 1) }
                .onSuccess { movies ->
                    updateSection(category) { it.copy(movies = movies, isLoading = false, error = null, page = 1, visibleCount = 10) }
                }
                .onFailure {
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
            val nextPage = current.page + 1
            updateSection(category) { it.copy(isLoading = true, error = null) }
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

    fun visibleMovies(state: SectionState): List<Movie> = state.movies.take(state.visibleCount)

    private suspend fun fetchCategory(category: String, page: Int): List<Movie> {
        val response = when (category) {
            "tv_discover" -> apiService.discoverTv(page)
            "tv_airing_today" -> apiService.getAiringTodayTv(page)
            "tv_on_the_air" -> apiService.getOnTheAirTv(page)
            "tv_popular" -> apiService.getPopularTv(page)
            "tv_top_rated" -> apiService.getTopRatedTv(page)
            else -> apiService.discoverTv(page)
        }
        return response.body()?.results.orEmpty().take(15)
    }

    private fun updateSection(category: String, reducer: (SectionState) -> SectionState) {
        val current = _sections.value ?: return
        val target = current[category] ?: return
        _sections.postValue(current + (category to reducer(target)))
    }
}
