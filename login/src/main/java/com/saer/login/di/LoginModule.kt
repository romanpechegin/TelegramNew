package com.saer.login.di

import com.saer.core.common.InputMask
import com.saer.core.communications.Communication
import com.saer.core.di.IoDispatcher
import com.saer.core.di.LoginFeature
import com.saer.core.di.MainDispatcher
import com.saer.login.mappers.MapperAuthorisationStateToEnterCodeUi
import com.saer.login.mappers.MapperAuthorisationStateToEnterPhoneUi
import com.saer.login.mappers.MapperAuthorisationStateToRegisterUi
import com.saer.login.repositories.AuthRepository
import com.saer.login.ui.EnterCodeUi
import com.saer.login.ui.EnterPasswordUi
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
    fun provideEnterPhoneCommunication(): Communication<EnterPhoneUi> =
        Communication.StateFlow(EnterPhoneUi.WaitEnterPhoneUi())

    @Provides
    @LoginFeature
    fun provideInputMaskCommunication(): Communication<InputMask> =
        Communication.StateFlow(InputMask.EmptyMask())

    @Provides
    @LoginFeature
    fun provideEnterCodeCommunication(): Communication<EnterCodeUi> =
        Communication.StateFlow(EnterCodeUi.WaitCodeUi())

    @Provides
    @LoginFeature
    fun provideRegistrationCommunication(): Communication<RegisterUi> =
        Communication.StateFlow(RegisterUi.WaitEnterNameUi())

    @Provides
    @LoginFeature
    fun provideEnterPasswordUiCommunication(): Communication<EnterPasswordUi> =
        Communication.StateFlow(EnterPasswordUi.Wait())

    @Provides
    fun provideMapperAuthorisationStateToEnterPhoneUi(): MapperAuthorisationStateToEnterPhoneUi =
        MapperAuthorisationStateToEnterPhoneUi.Base()

    @Provides
    fun provideMapperAuthorisationStateToEnterCodeUi(): MapperAuthorisationStateToEnterCodeUi =
        MapperAuthorisationStateToEnterCodeUi.Base()

    @Provides
    fun provideMapperAuthorisationStateToRegisterUi(): MapperAuthorisationStateToRegisterUi =
        MapperAuthorisationStateToRegisterUi.Base()

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