package com.saer.telegramnew.di

import com.saer.telegramnew.MainActivity
import com.saer.telegramnew.ui.MainFragment
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
}

@Module
class AppModule {

}

@Module
class NetworkModule {

}