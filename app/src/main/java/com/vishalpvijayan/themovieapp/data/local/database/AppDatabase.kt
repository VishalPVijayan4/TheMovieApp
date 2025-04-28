package com.vishalpvijayan.themovieapp.data.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.vishalpvijayan.themovieapp.data.local.dao.UserDao
import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
