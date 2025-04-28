package com.vishalpvijayan.themovieapp.domain.repository

import androidx.paging.PagingData
import com.vishalpvijayan.themovieapp.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun getUsers(): Flow<PagingData<User>>
    suspend fun addUser(user: User): Result<Unit>
    suspend fun syncOfflineUsers()   // <-- ADD THIS
}