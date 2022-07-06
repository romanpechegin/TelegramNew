package com.saer.login.di

import android.app.Application
import android.content.Context
import com.saer.core.Communication
import com.saer.login.repositories.AuthRepository
import com.saer.login.ui.EnterCodeUi
import com.saer.login.ui.EnterPhoneUi
import com.saer.login.ui.RegisterUi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.telegram.core.TelegramFlow

@Module
class LoginModule {
    @Provides
    fun provideResultCommunication(): Communication<EnterPhoneUi> =
        Communication.Base(EnterPhoneUi.WaitEnterPhoneUi())

    @Provides
    fun provideEnterCodeCommunication(): Communication<EnterCodeUi> =
        Communication.Base(EnterCodeUi.WaitCodeUi())

    @Provides
    fun provideRegistrationCommunication(): Communication<RegisterUi> =
        Communication.Base(RegisterUi.WaitEnterNameUi())

    @Provides
    fun provideAuthRepository(api: TelegramFlow): AuthRepository = AuthRepository.Base(api)

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideMainDispatcher(): MainCoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}