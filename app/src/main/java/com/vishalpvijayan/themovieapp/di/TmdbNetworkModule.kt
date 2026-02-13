package com.vishalpvijayan.themovieapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TmdbNetworkModule {

    @Provides
    @Singleton
    @Named("TmdbOkHttp")
    fun provideTmdbOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("language", "en-US")
                    .addQueryParameter("page", "1")
                    .addQueryParameter("api_key", "b26ed35ac317e85bbd0b2396ebaeb918")
                    .build()
                val request = original.newBuilder().url(url).build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }


    @Provides
    @Singleton
    @Named("TmdbRetrofit")
    fun provideTmdbRetrofit(@Named("TmdbOkHttp") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}


