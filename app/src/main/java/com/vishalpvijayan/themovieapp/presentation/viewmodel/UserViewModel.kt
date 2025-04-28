package com.vishalpvijayan.themovieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import com.vishalpvijayan.themovieapp.domain.usecase.AddUserUseCase
import com.vishalpvijayan.themovieapp.domain.usecase.SyncUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    // Expose users as a Flow with caching
    val users: Flow<PagingData<User>> = userRepository.getUsers()
        .cachedIn(viewModelScope)

    fun addUser(name: String, job: String) {
        viewModelScope.launch {
            val user = User(fullName = name, position = job)
            addUserUseCase(user)
        }
    }

    fun syncUsers() {
        viewModelScope.launch {
            userRepository.syncOfflineUsers()
        }
    }
}





