package com.saer.telegramnew.di

import android.app.Application
import android.content.Context
import com.saer.api.ApiModule
import com.saer.core.di.AppScope
import com.saer.core.di.CoreModule
import com.saer.login.di.LoginDeps
import com.saer.login.di.LoginModule
import com.saer.telegramnew.MainActivity
import com.saer.telegramnew.MainActivityViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@AppScope
@Component(
    modules = [
        AppModule::class,
        ApiModule::class,
        CoreModule::class,
        LoginModule::class
    ]
)
interface AppComponent : LoginDeps, ChatsDeps {
    fun inject(activity: MainActivity)

    fun mainActivityViewModel(): MainActivityViewModel.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

@Module
class AppModule {

    @AppScope
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}