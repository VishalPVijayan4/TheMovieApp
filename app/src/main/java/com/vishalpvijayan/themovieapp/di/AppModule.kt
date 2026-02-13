package com.vishalpvijayan.themovieapp.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.WorkManager
import com.vishalpvijayan.themovieapp.data.local.dao.UserDao
import com.vishalpvijayan.themovieapp.data.local.database.AppDatabase
import com.vishalpvijayan.themovieapp.data.local.datasource.UserLocalDataSource
import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import com.vishalpvijayan.themovieapp.data.remote.datasource.MovieRemoteDataSource
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
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("ReqresOkHttp")
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val headerInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
//                .addHeader("x-api-key", "reqres-free-v1")
                .addHeader("x-api-key", "reqres_a78e2ad54ea34e0e9ebbb6a22d80edb7")
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
    @Named("ReqresRetrofit")
    fun provideRetrofit(@Named("ReqresOkHttp") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        @ReqresApi apiService: ApiService,
        @TmdbApi tmdbApiService: ApiService
    ): UserRemoteDataSource {
        return UserRemoteDataSource(
            apiService,
            tmdbApiService = tmdbApiService
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "user_database"
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
    fun provideWorkManagerConfiguration(workerFactory: HiltWorkerFactory): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSource: UserRemoteDataSource,
        localDataSource: UserLocalDataSource,
        movieRemoteDataSource: MovieRemoteDataSource,
        workManager: WorkManager
    ): UserRepository {
        return UserRepositoryImpl(remoteDataSource, localDataSource, movieRemoteDataSource, workManager)
    }
}
