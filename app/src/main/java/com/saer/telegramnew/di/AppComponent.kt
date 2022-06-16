package com.saer.telegramnew.di

import android.content.Context
import com.saer.telegramnew.App
import com.saer.telegramnew.MainActivity
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.auth.communication.EnterCodeUiCommunication
import com.saer.telegramnew.auth.communication.EnterPhoneUiCommunication
import com.saer.telegramnew.auth.communication.RegistrationUiCommunication
import com.saer.telegramnew.auth.interactors.AuthRepository
import com.saer.telegramnew.auth.ui.EnterCodeFragment
import com.saer.telegramnew.auth.ui.EnterPhoneNumberFragment
import com.saer.telegramnew.auth.ui.RegistrationFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.telegram.core.TelegramFlow
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, CommunicationModule::class, CommonModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: EnterPhoneNumberFragment)
    fun inject(fragment: EnterCodeFragment)
    fun inject(registrationFragment: RegistrationFragment)
}

@Module
class AppModule(private val application: App) {

    @Provides
    fun provideContext(): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideAuthRepository(telegramFlow: TelegramFlow): AuthRepository =
        AuthRepository.Base(telegramFlow)

}

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideTelegramFlow(): TelegramFlow {
        val api = TelegramFlow()
        api.attachClient()
        return api
    }
}

@Module
class CommunicationModule {

    @Provides
    fun provideResultCommunication(): EnterPhoneUiCommunication =
        EnterPhoneUiCommunication.Base()

    @Provides
    fun provideEnterCodeCommunication(): EnterCodeUiCommunication =
        EnterCodeUiCommunication.Base()

    @Provides
    fun provideRegistrationCommunication(): RegistrationUiCommunication =
        RegistrationUiCommunication.Base()
}

@Module
class CommonModule {

    @Provides
    fun provideResources(context: Context): Resources = Resources.Base(context)

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideMainDispatcher(): MainCoroutineDispatcher = Dispatchers.Main

}