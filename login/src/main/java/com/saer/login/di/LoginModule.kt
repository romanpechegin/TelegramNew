package com.saer.login.di

import com.saer.core.Communication
import com.saer.core.di.IoDispatcher
import com.saer.core.di.LoginFeature
import com.saer.core.di.MainDispatcher
import com.saer.login.repositories.AuthRepository
import com.saer.login.ui.EnterCodeUi
import com.saer.login.ui.EnterPhoneUi
import com.saer.login.ui.RegisterUi
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

@Module(includes = [LoginBindModule::class])
class LoginModule {
    @Provides
    @LoginFeature
    fun provideResultCommunication(): Communication<EnterPhoneUi> =
        Communication.Base(EnterPhoneUi.WaitEnterPhoneUi())

    @Provides
    @LoginFeature
    fun provideEnterCodeCommunication(): Communication<EnterCodeUi> =
        Communication.Base(EnterCodeUi.WaitCodeUi())

    @Provides
    @LoginFeature
    fun provideRegistrationCommunication(): Communication<RegisterUi> =
        Communication.Base(RegisterUi.WaitEnterNameUi())

    @Provides
    @LoginFeature
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @LoginFeature
    @MainDispatcher
    fun provideMainDispatcher(): MainCoroutineDispatcher = Dispatchers.Main
}

@Module
interface LoginBindModule {
    @Binds
    fun bindAuthRepository(authRepositoryBase: AuthRepository.Base): AuthRepository
}