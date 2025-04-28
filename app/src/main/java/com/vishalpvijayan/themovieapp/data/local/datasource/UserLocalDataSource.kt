package com.vishalpvijayan.themovieapp.data.local.datasource

import com.vishalpvijayan.themovieapp.data.local.dao.UserDao
import com.vishalpvijayan.themovieapp.data.local.mapper.toDomain
import com.vishalpvijayan.themovieapp.data.local.mapper.toEntity
import com.vishalpvijayan.themovieapp.domain.model.User
import javax.inject.Inject

//class UserLocalDataSource @Inject constructor(
//    private val userDao: UserDao
//) {
//
//    suspend fun insertUser(user: User) {
//        userDao.insert(user)
//    }
//
//
//    suspend fun getUnsyncedUsers(): List<User> {
//        return userDao.getUnsyncedUsers()
//    }
//
//    suspend fun markUserAsSynced(user: User) {
//        userDao.markUserAsSynced(user)
//    }
//}



class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user.toEntity()) // Map domain → entity
    }

    suspend fun getUnsyncedUsers(): List<User> {
        return userDao.getUnsyncedUsers().map { it.toDomain() } // Map entity → domain
    }

    suspend fun markUserAsSynced(user: User) {
        userDao.updateUser(user.copy(isSynced = true).toEntity()) // Update sync flag
    }
}

