package com.vishalpvijayan.themovieapp.data.local.mapper



import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
import com.vishalpvijayan.themovieapp.domain.model.User

// Entity → Domain
fun UserEntity.toDomain(): User {
    return User(
        id = userId,
        fullName = name,
        position = job,
        profilePic = avatar,
        isSynced = isSynced
    )
}

// Domain → Entity (for saving to database)
fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = id,
        name = fullName,
        job = position,
        avatar = profilePic,
        isSynced = isSynced
    )
}