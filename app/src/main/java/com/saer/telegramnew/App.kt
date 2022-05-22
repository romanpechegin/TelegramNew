package com.saer.telegramnew

import android.app.Application
import android.content.Context
import com.saer.telegramnew.di.AppComponent
import com.saer.telegramnew.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }