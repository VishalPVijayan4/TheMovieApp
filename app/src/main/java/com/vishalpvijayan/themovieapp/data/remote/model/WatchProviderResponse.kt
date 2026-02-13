package com.vishalpvijayan.themovieapp.data.remote.model

data class WatchProviderResponse(
    val id: Int,
    val results: Map<String, CountryWatchProvider> = emptyMap()
)

data class CountryWatchProvider(
    val link: String? = null
)
