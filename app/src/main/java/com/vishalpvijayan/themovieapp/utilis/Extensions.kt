package com.vishalpvijayan.themovieapp.utilis

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(nw) ?: return false
        caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        cm.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}

