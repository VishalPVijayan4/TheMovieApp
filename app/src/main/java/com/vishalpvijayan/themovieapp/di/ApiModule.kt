package com.vishalpvijayan.themovieapp.di

import com.vishalpvijayan.themovieapp.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApiService(@Named("ReqresRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

