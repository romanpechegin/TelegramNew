package com.saer.telegramnew.di

import android.content.Context
import com.saer.telegramnew.App
import com.saer.telegramnew.MainActivity
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.ResultCheckPhoneCommunication
import com.saer.telegramnew.communications.ResultSendCodeCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import com.saer.telegramnew.interactors.AuthRepository
import com.saer.telegramnew.ui.EnterPhoneNumberFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, CommunicationModule::class, CommonModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: EnterPhoneNumberFragment)
}

@Module
class AppModule(private val application: App) {

    @Provides
    fun provideContext(): Context {
        return application.applicationContext
    }

    @Provides
    fun provideAuthInteractor(authRepository: AuthRepository): AuthInteractor =
        AuthInteractor.Base(authRepository)

    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepository.Base()

}

@Module
class NetworkModule {

}

@Module
class CommunicationModule {

    @Provides
    fun provideResultCommunication(): ResultCheckPhoneCommunication = ResultCheckPhoneCommunication.Base()

    @Provides
    fun provideResultInputPhoneCommunication(): ResultSendCodeCommunication =
        ResultSendCodeCommunication.Base()

}

@Module
class CommonModule {

    @Provides
    fun provideResources(context: Context): Resources = Resources.Base(context)

}