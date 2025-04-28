package com.vishalpvijayan.themovieapp.data.remote.datasource

import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.model.UserRequest
import com.vishalpvijayan.themovieapp.data.remote.model.UsersResponse
import javax.inject.Inject

//class UserRemoteDataSource @Inject constructor(
//    private val apiService: ApiService
//) {
//    suspend fun getUsers(page: Int): UsersResponse = apiService.getUsers(page)
//}

class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getUsers(page: Int): UsersResponse {
        // Make the network call and return the result
        return apiService.getUsers(page)
    }

    suspend fun addUser(request: UserRequest) = apiService.addUser(request)
}


