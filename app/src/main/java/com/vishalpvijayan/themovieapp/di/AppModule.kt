package com.vishalpvijayan.themovieapp.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.vishalpvijayan.themovieapp.data.local.dao.UserDao
import com.vishalpvijayan.themovieapp.data.local.database.AppDatabase
import com.vishalpvijayan.themovieapp.data.local.datasource.UserLocalDataSource
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.datasource.UserRemoteDataSource
import com.vishalpvijayan.themovieapp.data.repository.UserRepositoryImpl
import com.vishalpvijayan.themovieapp.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val headerInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("x-api-key", "reqres-free-v1")
                .build()
            chain.proceed(newRequest)
        }

        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(apiService: ApiService): UserRemoteDataSource {
        return UserRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "user_database" // Give a meaningful name if you want
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideUserLocalDataSource(userDao: UserDao): UserLocalDataSource {
        return UserLocalDataSource(userDao)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSource: UserRemoteDataSource,
        localDataSource: UserLocalDataSource,
        workManager: WorkManager
    ): UserRepository {
        return UserRepositoryImpl(remoteDataSource, localDataSource, workManager)
    }

}
