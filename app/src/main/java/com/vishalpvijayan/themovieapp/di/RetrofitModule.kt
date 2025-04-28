//package com.vishalpvijayan.themovieapp.di
//
//
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//import javax.inject.Singleton
//import dagger.Module
//import dagger.Provides
//
//
//
//@Module
//class RetrofitModule {
//
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
////        loggingInterceptor.level = if (BuildConfig.DEBUG) {
////            HttpLoggingInterceptor.Level.BODY // Logs request/response body in debug mode
////        } else {
////            HttpLoggingInterceptor.Level.NONE // Disable logging in production
////        }
//
//        return OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl("https://reqres.in/") // Base URL of the API
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create()) // Gson converter for JSON to object conversion
//            .build()
//    }
//}
