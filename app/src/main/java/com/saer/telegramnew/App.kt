package com.saer.telegramnew

import android.app.Application
import com.saer.login.di.LoginDepsProvider
import com.saer.telegramnew.di.AppComponent
import com.saer.telegramnew.di.DaggerAppComponent

class App : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        LoginDepsProvider.deps = appComponent
    }
}