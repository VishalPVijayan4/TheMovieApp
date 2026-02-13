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

    private const val TMDB_BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiMjZlZDM1YWMzMTdlODViYmQwYjIzOTZlYmFlYjkxOCIsIm5iZiI6MTU0Mzg1Njc5NS4zNiwic3ViIjoiNWMwNTYyOWIwZTBhMjYzM2E2MGNjN2ZmIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.J5VYtii0CZLZeC_92MMepwXdsMV163TNYaZFVYKrUpA"

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
                    .build()

                val request = original.newBuilder()
                    .url(url)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", TMDB_BEARER_TOKEN)
                    .build()

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
