package com.vishalpvijayan.themovieapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
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

    private val _unsyncedUsers = MutableLiveData<List<UserEntity>>()
    val unsyncedUsers: LiveData<List<UserEntity>> = _unsyncedUsers

    fun getUnsyncedUsers() {
        viewModelScope.launch {
            userRepository.getOfflineUnsyncedUsers().collect {
                _unsyncedUsers.value = it
            }
        }
    }

    fun syncSingleUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.syncSingleUser(user)
            getUnsyncedUsers() // Refresh list
        }
    }

    // Expose users as a Flow with caching
    val users: Flow<PagingData<User>> = userRepository.getUsers()
        .cachedIn(viewModelScope)

    fun addUser(name: String, job: String) {
        viewModelScope.launch {
            val user = User(fullName = name, position = job)
            Log.d("UserViewModel", "Adding user: name=$name, job=$job")
            addUserUseCase(user)
        }
    }

    fun syncUsers() {
        viewModelScope.launch {
            userRepository.syncOfflineUsers()
            Log.d("SyncUserWorker", "Offline users synced successfully.")
        }
    }
}





