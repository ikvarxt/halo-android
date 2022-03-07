package me.ikvarxt.halo

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

lateinit var application: Context

@HiltAndroidApp
class HaloApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }
}