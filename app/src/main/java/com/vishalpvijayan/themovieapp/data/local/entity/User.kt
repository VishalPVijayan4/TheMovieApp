//package com.vishalpvijayan.themovieapp.data.local.entity
//
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
////@Entity(tableName = "users")
////data class LocalUserEntity(
////    @PrimaryKey(autoGenerate = true) val id: Int = 0,
////    val name: String,
////    val job: String,
////    val serverId: String? = null // When synced, server will provide an ID
////)
//
////@Entity(tableName = "users")
////data class User(
////    @PrimaryKey(autoGenerate = true)
////    val id: Long = 0,
////    val name: String,
////    val job: String,
////    val isSynced: Boolean = false // Flag to check if the user is synced
////)
////
////@Entity(tableName = "users") // Room annotation for defining the database table
////data class User(
////    @PrimaryKey(autoGenerate = true) // Room annotation for the primary key, auto-generated
////    val userId: Int = 0, // This is the userId field, and Room will generate it
////
////    @ColumnInfo(name = "name") // Room annotation for the column name
////    val name: String,
////
////    @ColumnInfo(name = "job") // Room annotation for the column name
////    val job: String,
////
////    @ColumnInfo(name = "avatar") // Column for avatar image or URL
////    val avatar: String? = null
////)
//
//@Entity(tableName = "users") // Room annotation for defining the database table
//data class User(
//    @PrimaryKey(autoGenerate = true) // Room annotation for the primary key, auto-generated
//    val userId: Int = 0, // This is the userId field, and Room will generate it
//
//    @ColumnInfo(name = "name") // Room annotation for the column name
//    val name: String,
//
//    @ColumnInfo(name = "job") // Room annotation for the column name
//    val job: String,
//
//    @ColumnInfo(name = "avatar") // Column for avatar image or URL
//    val avatar: String? = null,
//
//    @ColumnInfo(name = "isSynced") // Flag to check if the user is synced
//    val isSynced: Boolean = false // Default value is false, indicating the user is not synced
//)
//
