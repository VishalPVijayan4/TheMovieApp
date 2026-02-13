package com.vishalpvijayan.themovieapp.data.remote.model

data class AuthTokenResponse(
    val success: Boolean,
    val request_token: String,
    val expires_at: String? = null
)

data class LoginRequest(
    val username: String,
    val password: String,
    val request_token: String
)

data class SessionRequest(
    val request_token: String
)

data class SessionResponse(
    val success: Boolean,
    val session_id: String
)

data class DeleteSessionRequest(
    val session_id: String
)

data class TmdbStatusResponse(
    val success: Boolean? = null,
    val status_code: Int? = null,
    val status_message: String? = null
)
