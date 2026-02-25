package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.EpisodeGroupItem
import com.vishalpvijayan.themovieapp.data.remote.model.CreditPerson
import com.vishalpvijayan.themovieapp.data.remote.model.FavoriteRequest
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.data.remote.model.TvSeriesDetail
import com.vishalpvijayan.themovieapp.data.remote.model.VideoItem
import com.vishalpvijayan.themovieapp.di.TmdbApi
import com.vishalpvijayan.themovieapp.utilis.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvSeriesDetailViewModel @Inject constructor(
    @TmdbApi private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _detail = MutableLiveData<TvSeriesDetail?>()
    val detail: LiveData<TvSeriesDetail?> = _detail

    private val _videos = MutableLiveData<List<VideoItem>>(emptyList())
    val videos: LiveData<List<VideoItem>> = _videos

    private val _episodeGroups = MutableLiveData<List<EpisodeGroupItem>>(emptyList())
    val episodeGroups: LiveData<List<EpisodeGroupItem>> = _episodeGroups

    private val _similar = MutableLiveData<List<Movie>>(emptyList())
    val similar: LiveData<List<Movie>> = _similar

    private val _credits = MutableLiveData<List<CreditPerson>>(emptyList())
    val credits: LiveData<List<CreditPerson>> = _credits

    private val _trailerKey = MutableLiveData<String?>(null)
    val trailerKey: LiveData<String?> = _trailerKey

    private val _watchProvidersText = MutableLiveData("Watch providers unavailable")
    val watchProvidersText: LiveData<String> = _watchProvidersText
    private val _watchProvidersLink = MutableLiveData<String?>(null)
    val watchProvidersLink: LiveData<String?> = _watchProvidersLink

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _favorite = MutableLiveData(false)
    val favorite: LiveData<Boolean> = _favorite

    fun load(seriesId: Int) {
        viewModelScope.launch {
            _loading.postValue(true)
            _error.postValue(null)
            runCatching {
                val detailResp = apiService.getTvSeriesDetails(seriesId)
                val videosResp = apiService.getTvSeriesVideos(seriesId)
                val groupsResp = apiService.getTvEpisodeGroups(seriesId)
                val similarResp = apiService.getSimilarTvSeries(seriesId)
                val creditsResp = apiService.getTvCredits(seriesId)
                val providersResp = apiService.getTvWatchProviders(seriesId)

                _detail.postValue(detailResp.body())
                val videos = videosResp.body()?.results.orEmpty()
                _videos.postValue(videos)
                _trailerKey.postValue(videos.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }?.key)
                _episodeGroups.postValue(groupsResp.body()?.results.orEmpty())
                _similar.postValue(similarResp.body()?.results.orEmpty().take(15))
                _credits.postValue(creditsResp.body()?.cast.orEmpty().take(15))
                val providers = providersResp.body()?.results.orEmpty()
                if (providers.isNotEmpty()) {
                    _watchProvidersText.postValue("Where to watch: " + providers.keys.take(6).joinToString())
                    _watchProvidersLink.postValue(providers.values.firstOrNull()?.link)
                }
            }.onFailure {
                _error.postValue("Failed to load TV details")
            }
            _loading.postValue(false)
        }
    }

    fun toggleFavorite(seriesId: Int) {
        val sessionId = sessionManager.getSessionId() ?: run {
            _error.postValue("Login required to manage favorites")
            return
        }
        val newValue = !(_favorite.value ?: false)
        viewModelScope.launch {
            runCatching {
                apiService.setFavorite(
                    request = FavoriteRequest(media_type = "tv", media_id = seriesId, favorite = newValue),
                    sessionId = sessionId
                )
            }.onSuccess {
                _favorite.postValue(newValue)
            }.onFailure {
                _error.postValue("Failed to update favorite")
            }
        }
    }
}
