package com.medical.buganddrug

import android.app.Application
import com.medical.buganddrug.data.remote.initKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)
        }
    }
}