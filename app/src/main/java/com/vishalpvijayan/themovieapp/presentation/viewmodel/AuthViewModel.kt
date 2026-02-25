package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.DeleteSessionRequest
import com.vishalpvijayan.themovieapp.data.remote.model.LoginRequest
import com.vishalpvijayan.themovieapp.data.remote.model.SessionRequest
import com.vishalpvijayan.themovieapp.di.TmdbApi
import com.vishalpvijayan.themovieapp.utilis.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @TmdbApi private val tmdbApiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _authMessage = MutableLiveData<String?>()
    val authMessage: LiveData<String?> = _authMessage

    private val _loggedIn = MutableLiveData(sessionManager.isLoggedIn())
    val loggedIn: LiveData<Boolean> = _loggedIn

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val tokenResponse = tmdbApiService.createRequestToken()
                val requestToken = tokenResponse.body()?.request_token
                if (!tokenResponse.isSuccessful || requestToken.isNullOrBlank()) {
                    _authMessage.postValue("Unable to create request token")
                    return@launch
                }

                val validated = tmdbApiService.validateWithLogin(
                    LoginRequest(username = username, password = password, request_token = requestToken)
                )
                val validatedToken = validated.body()?.request_token
                if (!validated.isSuccessful || validatedToken.isNullOrBlank()) {
                    _authMessage.postValue("Invalid username or password")
                    return@launch
                }

                val session = tmdbApiService.createSession(SessionRequest(validatedToken))
                val sessionId = session.body()?.session_id
                if (session.isSuccessful && !sessionId.isNullOrBlank()) {
                    sessionManager.saveSessionId(sessionId)
                    sessionManager.saveExpiry(tokenResponse.body()?.expires_at)
                    val accountResp = tmdbApiService.getCurrentAccountDetails(sessionId)
                    accountResp.body()?.id?.let { sessionManager.saveAccountId(it) }
                    _loggedIn.postValue(true)
                    _authMessage.postValue("Login successful")
                } else {
                    _authMessage.postValue("Failed to create session")
                }
            } catch (e: Exception) {
                _authMessage.postValue(e.message ?: "Login failed")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val sessionId = sessionManager.getSessionId()
                if (!sessionId.isNullOrBlank()) {
                    tmdbApiService.deleteSession(DeleteSessionRequest(sessionId))
                }
            } finally {
                sessionManager.clearSession()
                _loggedIn.postValue(false)
                _isLoading.postValue(false)
            }
        }
    }
}
