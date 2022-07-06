package com.saer.telegramnew.di

import android.app.Application
import android.content.Context
import com.saer.api.ApiModule
import com.saer.core.di.CoreModule
import com.saer.login.di.LoginDeps
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    modules = [
        AppModule::class,
        ApiModule::class,
        CoreModule::class
    ]
)
@AppScope
interface AppComponent : LoginDeps {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

@Module
class AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}

@Scope
annotation class AppScope