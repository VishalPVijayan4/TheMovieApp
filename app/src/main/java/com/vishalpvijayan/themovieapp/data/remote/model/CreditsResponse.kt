package com.vishalpvijayan.themovieapp.data.remote.model

data class CreditsResponse(
    val cast: List<CreditPerson> = emptyList()
)

data class CreditPerson(
    val id: Int,
    val name: String? = null,
    val character: String? = null,
    val profile_path: String? = null
)
