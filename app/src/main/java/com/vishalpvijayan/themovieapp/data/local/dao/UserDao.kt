package com.vishalpvijayan.themovieapp.data.local.dao


import androidx.room.*
import com.vishalpvijayan.themovieapp.domain.model.User




//@Dao
//interface UserDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(user: User)
//
//    @Query("SELECT * FROM users WHERE isSynced = 0")
//    suspend fun getUnsyncedUsers(): List<User>
//
//    @Query("UPDATE users SET isSynced = 1 WHERE userId = :user")
//    suspend fun markUserAsSynced(user: User)
//}

//package com.vishalpvijayan.themovieapp.data.local.dao

import androidx.room.*
import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
//import com.vishalpvijayan.themovieapp.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE isSynced = 0")
    suspend fun getUnsyncedUsers(): List<UserEntity>
}

