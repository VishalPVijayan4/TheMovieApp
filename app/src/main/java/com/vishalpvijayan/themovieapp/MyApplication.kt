package com.vishalpvijayan.themovieapp

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        WorkManager.initialize(this, Configuration.Builder().build())
    }
}