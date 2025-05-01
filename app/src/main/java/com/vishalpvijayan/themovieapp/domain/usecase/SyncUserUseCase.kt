package com.vishalpvijayan.themovieapp.domain.usecase

import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import javax.inject.Inject

/*class SyncUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() {
        repository.syncOfflineUsers()
    }
}*/


class SyncUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        userRepository.syncOfflineUsers(user)
    }
}