package com.vishalpvijayan.themovieapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import com.vishalpvijayan.themovieapp.domain.usecase.AddUserUseCase
import com.vishalpvijayan.themovieapp.domain.usecase.SyncUserUseCase
import com.vishalpvijayan.themovieapp.workers.SyncUserWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository,
    private val syncUserUseCase: SyncUserUseCase
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    val pagedUsers: Flow<PagingData<User>> = userRepository.getUsers()
        .cachedIn(viewModelScope)

    val users = userRepository.getOfflineUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun syncSingleUser(user: User) {
        viewModelScope.launch {
            syncUserUseCase(user)
        }
    }



    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
            scheduleSyncWorker()
        }
    }

    private val _unsyncedUsers = MutableLiveData<List<UserEntity>>()
    val unsyncedUsers: LiveData<List<UserEntity>> = _unsyncedUsers

    fun getUnsyncedUsers() {
        viewModelScope.launch {
            userRepository.getOfflineUnsyncedUsers().collect {
                _unsyncedUsers.value = it
            }
        }
    }

    private fun scheduleSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncUserWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "sync_user_work",
            ExistingWorkPolicy.KEEP,
            request
        )
    }
}


/*@HiltViewModel
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

    *//*private val _unsyncedUsers = MutableLiveData<List<User>>()
    val unsyncedUsers: LiveData<List<User>> = _unsyncedUsers

    fun getUnsyncedUsers() {
        viewModelScope.launch {
            val result = userRepository.getOfflineUsers()
            _unsyncedUsers.value = result
        }
    }*//*

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
}*/





