package com.example.adminmovile

import android.app.Application
import com.example.adminmovile.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AdminApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AdminApp)
            modules(appModule)
        }
    }
}
