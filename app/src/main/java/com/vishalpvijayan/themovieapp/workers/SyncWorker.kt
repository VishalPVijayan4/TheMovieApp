package com.vishalpvijayan.themovieapp.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vishalpvijayan.themovieapp.data.local.dao.UserDao
import com.vishalpvijayan.themovieapp.data.local.mapper.toDomain
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.domain.model.toRequest
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltWorker
class SyncUserWorker @Inject constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val apiService: ApiService,
    private val userDao: UserDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncUserWorker", "WorkManager triggered to sync offline users.")
        return try {
            val usersOffline = userDao.getUnsyncedUsers().first()

            for (user in usersOffline) {
                val response = apiService.addUser(user.toDomain().toRequest())

                if (response.isSuccessful) {
                    userDao.updateUser(user.copy(isSynced = true))
                }
            }


            Result.success()
        } catch (exception: Exception) {
            Log.d("Exception while sync",exception.toString())
            Result.retry()
        }
    }
}


