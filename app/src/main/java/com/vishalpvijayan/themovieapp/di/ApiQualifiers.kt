package com.vishalpvijayan.themovieapp.di


import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReqresApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbApi