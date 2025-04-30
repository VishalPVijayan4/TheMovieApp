package com.vishalpvijayan.themovieapp.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vishalpvijayan.themovieapp.data.local.datasource.UserLocalDataSource
import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
import com.vishalpvijayan.themovieapp.data.remote.datasource.UserPagingSource
import com.vishalpvijayan.themovieapp.data.remote.datasource.UserRemoteDataSource
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.domain.model.toRequest
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import com.vishalpvijayan.themovieapp.workers.SyncUserWorker
import kotlinx.coroutines.flow.Flow

//import android.util.Log
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.WorkManager
//import com.vishalpvijayan.themovieapp.data.local.datasource.UserLocalDataSource
//import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
//import com.vishalpvijayan.themovieapp.data.remote.datasource.UserPagingSource
//import com.vishalpvijayan.themovieapp.data.remote.datasource.UserRemoteDataSource
//
//import com.vishalpvijayan.themovieapp.domain.model.toRequest
//import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
//import com.vishalpvijayan.themovieapp.utils.worker.SyncUserWorker
//import kotlinx.coroutines.flow.Flow
//import com.vishalpvijayan.themovieapp.data.local.mapper.toDomain
//import com.vishalpvijayan.themovieapp.domain.model.User

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val workManager: WorkManager
) : UserRepository {

    override fun getUsers(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 6),
            pagingSourceFactory = { UserPagingSource(remoteDataSource) }
        ).flow
    }

    override suspend fun addUser(user: User): Result<Unit> {
        Log.d("UserRepository", "Attempting to add user: $user")
        return try {
            val response = remoteDataSource.addUser(user.toRequest())
            Log.d("UserRepository", "API response: ${response.code()} ${response.message()}")

            if (response.isSuccessful) {
                Log.d("UserRepository", "User added successfully on server.")
                Result.success(Unit)
            } else {
                Log.d("UserRepository", "Failed to add user online, saving offline.")
                localDataSource.insertUser(user)
                enqueueSyncUserWork()
                Result.success(Unit)
            }
        } catch (exception: Exception) {
            Log.e("UserRepository", "Exception during addUser: ${exception.message}")
            localDataSource.insertUser(user)
            enqueueSyncUserWork()
            Result.success(Unit)
        }
    }

    override suspend fun syncOfflineUsers() {
        Log.d("UserRepository", "Starting sync of offline users")
        val unsyncedUsers = localDataSource.getUnsyncedUsers()

        unsyncedUsers.forEach { user ->
            Log.d("UserRepository", "Syncing user: $user")
            val response = remoteDataSource.addUser(user.toRequest())
            if (response.isSuccessful) {
                Log.d("UserRepository", "User synced successfully: ${user.fullName}")
                localDataSource.markUserAsSynced(user)
            } else {
                Log.e("UserRepository", "Failed to sync user: ${user.fullName}")
            }
        }
    }

    override fun getOfflineUnsyncedUsers(): Flow<List<UserEntity>> {
        return localDataSource.getUnsyncedUsersFlow()
    }

    override suspend fun syncSingleUser(user: UserEntity) {
        try {
            val response = remoteDataSource.addUser(
                User(fullName = user.name, position = user.job).toRequest()
            )
            if (response.isSuccessful) {
                val updatedUser = user.copy(isSynced = true)
                localDataSource.updateUser(updatedUser)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error syncing single user: ${e.message}")
        }
    }

    override suspend fun getOfflineUsers(): List<User> {
        return localDataSource.getUnsyncedUsers().map { it }
    }


    private fun enqueueSyncUserWork() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncUserWorker>()
            .build()
        workManager.enqueue(syncRequest)
    }
}


