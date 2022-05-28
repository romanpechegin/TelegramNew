package com.saer.telegramnew.di

import com.saer.telegramnew.MainActivity
import com.saer.telegramnew.communication.ResultCommunication
import com.saer.telegramnew.ui.EnterPhoneNumberFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, CommunicationModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: EnterPhoneNumberFragment)
}

@Module
class AppModule {

}

@Module
class NetworkModule {

}

@Module
class CommunicationModule {

    @Provides
    fun provideResultCommunication(): ResultCommunication = ResultCommunication.Base()
}