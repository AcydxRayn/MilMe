package com.ks49.milme

import android.app.Application
import timber.log.Timber

class MilMeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            // Start Timber debug logging
            Timber.plant(Timber.DebugTree())
        } else {
            // TODO-AGM: Handle production ready logging & Analytics
        }
    }
}