package com.vishalpvijayan.themovieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,        // Primary Key (auto increment)
    val name: String,
    val job: String,
    val avatar: String? = null,
    val isSynced: Boolean = false // To track offline-sync
)