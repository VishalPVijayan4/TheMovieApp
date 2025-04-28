package com.vishalpvijayan.themovieapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vishalpvijayan.themovieapp.data.local.datasource.UserLocalDataSource
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.datasource.UserPagingSource
import com.vishalpvijayan.themovieapp.data.remote.datasource.UserRemoteDataSource
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.domain.model.toRequest
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import com.vishalpvijayan.themovieapp.utils.worker.SyncUserWorker
import kotlinx.coroutines.flow.Flow

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
        return try {
            val response = remoteDataSource.addUser(user.toRequest())

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                localDataSource.insertUser(user)
                enqueueSyncUserWork()
                Result.success(Unit)
            }
        } catch (exception: Exception) {
            localDataSource.insertUser(user)
            enqueueSyncUserWork()
            Result.success(Unit)
        }
    }

    override suspend fun syncOfflineUsers() {
        val unsyncedUsers = localDataSource.getUnsyncedUsers()

        unsyncedUsers.forEach { user ->
            val response = remoteDataSource.addUser(user.toRequest())
            if (response.isSuccessful) {
                localDataSource.markUserAsSynced(user)
            }
        }
    }

    private fun enqueueSyncUserWork() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncUserWorker>()
            .build()
        workManager.enqueue(syncRequest)
    }
}

