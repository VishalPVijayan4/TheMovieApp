package com.vishalpvijayan.themovieapp.domain.model


import com.vishalpvijayan.themovieapp.data.remote.model.UserRequest

fun User.toRequest(): UserRequest {
    return UserRequest(
        name = this.fullName,
        job = this.position
    )
}