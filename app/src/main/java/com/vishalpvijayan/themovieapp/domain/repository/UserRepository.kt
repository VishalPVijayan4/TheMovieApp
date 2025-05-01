package com.vishalpvijayan.themovieapp.domain.repository

import androidx.paging.PagingData
import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
import com.vishalpvijayan.themovieapp.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun getUsers(): Flow<PagingData<User>>
    suspend fun addUser(user: User): Result<Unit>
    suspend fun syncOfflineUsers(user: User)
    fun getOfflineUnsyncedUsers(): Flow<List<UserEntity>>
    suspend fun syncSingleUser(user: UserEntity)
//    suspend fun getOfflineUsers(): List<User>
fun getOfflineUsers(): Flow<List<User>>
}