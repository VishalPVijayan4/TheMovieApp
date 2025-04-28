package com.vishalpvijayan.themovieapp.utilis

import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
import com.vishalpvijayan.themovieapp.data.remote.model.UserDto
import com.vishalpvijayan.themovieapp.domain.model.User


// Converts remote model to domain model
fun UserDto.toUser(): User {
    return User(
        id = this.id,
        fullName = "${this.firstName} ${this.lastName}",  // If needed, concatenate first and last name
        position = this.lastName,  // Assuming job is part of the DTO, else adjust accordingly
        profilePic = this.avatar
    )
}

// Converts local entity to domain model
fun UserEntity.toDomainUser(): User {
    return User(
        id = userId,
        fullName = name,
        position = job,
        profilePic = avatar,
        isSynced = isSynced
    )
}


/*
fun UserDto.toUser(): User {
    return User(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        avatar = this.avatar
    )
}

fun UserEntity.toDomainUser(): User {
    return User(
        id = userId,
        fullName = name,
        position = job,
        profilePic = avatar,
        isSynced = isSynced,
        name = this.name
    )
}*/
