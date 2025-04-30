package com.vishalpvijayan.themovieapp.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _offlineUsers = MutableStateFlow<List<User>>(emptyList())
    val offlineUsers: StateFlow<List<User>> = _offlineUsers

    fun fetchOfflineUsers() {
        viewModelScope.launch {
            _offlineUsers.value = userRepository.getOfflineUsers()
        }
    }
}
