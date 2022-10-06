package com.saer.telegramnew

import android.app.Application
import android.os.Build
import com.google.android.material.color.DynamicColors
import com.saer.login.di.LoginDepsProvider
import com.saer.telegramnew.di.AppComponent
import com.saer.telegramnew.di.ChatsDepsProvider
import com.saer.telegramnew.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        LoginDepsProvider.deps = appComponent
        ChatsDepsProvider.deps = appComponent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DynamicColors.applyToActivitiesIfAvailable(this)
        }
    }
}