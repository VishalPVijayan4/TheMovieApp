package com.vishalpvijayan.themovieapp.data.local.datasource

import com.vishalpvijayan.themovieapp.data.local.dao.UserDao
import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
import com.vishalpvijayan.themovieapp.data.local.mapper.toEntity
import com.vishalpvijayan.themovieapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao
) {

    fun getUnsyncedUsersFlow(): Flow<List<UserEntity>> {
        return userDao.getUnsyncedUsers()
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user.toEntity()) // Map domain → entity
    }

    suspend fun getUnsyncedUsers(): List<User> {
        return userDao.getUnsyncedUsers().map { it } as List<User> // Map entity → domain
    }

    suspend fun markUserAsSynced(user: User) {
        userDao.updateUser(user.copy(isSynced = true).toEntity()) // Update sync flag
    }

    fun getAllUsers(): Flow<List<UserEntity>> {
        return userDao.getAllUsers()
    }

}

