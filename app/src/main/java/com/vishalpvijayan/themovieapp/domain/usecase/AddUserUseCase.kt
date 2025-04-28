package com.vishalpvijayan.themovieapp.domain.usecase


import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import javax.inject.Inject


class AddUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.addUser(user)
    }
}
