package com.vishalpvijayan.themovieapp.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vishalpvijayan.themovieapp.data.local.dao.UserDao
import com.vishalpvijayan.themovieapp.data.local.mapper.toDomain
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.di.ReqresApi
import com.vishalpvijayan.themovieapp.domain.model.toRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import javax.inject.Named

/*@HiltWorker
class SyncUserWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Named("Default") var apiService: ApiService,
    private val userDao: UserDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncUserWorker", "WorkManager triggered to sync offline users.")
        return try {
            val usersOffline = userDao.getUnsyncedUsers().first()

            for (user in usersOffline) {
                val response = apiService.addUser(user.toDomain().toRequest())
                if (response.isSuccessful) {

                    Log.d("SyncUserWorker", "Successfully synced user ${user.userId}")
                    userDao.updateUser(user.copy(isSynced = true))
                } else {
                    Log.e("SyncUserWorker", "Failed to sync user ${user.userId}: ${response.code()}")
                }
            }

            Result.success()
        } catch (exception: Exception) {
            Log.e("SyncUserWorker", "Exception during sync", exception)
            Result.retry()
        }
    }
}*/



@HiltWorker
class SyncUserWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    @ReqresApi private val apiService: ApiService,
    private val userDao: UserDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncUserWorker", "WorkManager triggered to sync offline users.")
        return try {
            val usersOffline = userDao.getUnsyncedUsers().first()

            for (user in usersOffline) {
                val response = apiService.addUser(user.toDomain().toRequest())
                if (response.isSuccessful) {
                    Log.d("SyncUserWorker", "Successfully synced user ${user.userId}")
                    userDao.updateUser(user.copy(isSynced = true))
                } else {
                    Log.e("SyncUserWorker", "Failed to sync user ${user.userId}: ${response.code()}")
                }
            }

            Result.success()
        } catch (exception: Exception) {
            Log.e("SyncUserWorker", "Exception during sync", exception)
            Result.retry()
        }
    }
}



