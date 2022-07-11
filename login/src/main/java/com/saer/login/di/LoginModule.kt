package com.saer.login.di

import com.saer.api.TelegramCredentials
import com.saer.core.Communication
import com.saer.core.di.AuthorisationFlowQualifier
import com.saer.core.di.Feature
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.telegram.core.TelegramFlow
import kotlinx.telegram.coroutines.checkDatabaseEncryptionKey
import kotlinx.telegram.coroutines.setTdlibParameters
import kotlinx.telegram.flows.authorizationStateFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState

@Module(includes = [LoginBindModule::class])
class LoginModule {
    @Provides
    @Feature
    fun provideResultCommunication(): Communication<EnterPhoneUi> =
        Communication.Base(EnterPhoneUi.WaitEnterPhoneUi())

    @Provides
    @Feature
    fun provideEnterCodeCommunication(): Communication<EnterCodeUi> =
        Communication.Base(EnterCodeUi.WaitCodeUi())

    @Provides
    @Feature
    fun provideRegistrationCommunication(): Communication<RegisterUi> =
        Communication.Base(RegisterUi.WaitEnterNameUi())

    @Provides
    @Feature
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Feature
    fun provideMainDispatcher(): MainCoroutineDispatcher = Dispatchers.Main

    @Feature
    @Provides
    @AuthorisationFlowQualifier
    fun authorizationFlow(api: TelegramFlow): Flow<AuthorizationState> {
        return api.authorizationStateFlow()
            .onEach {
                when (it) {
                    is TdApi.AuthorizationStateWaitTdlibParameters ->
                        api.setTdlibParameters(TelegramCredentials.parameters)
                    is TdApi.AuthorizationStateWaitEncryptionKey ->
                        api.checkDatabaseEncryptionKey(null)
                }
            }
    }
}

@Module
interface LoginBindModule {
    @Binds
    fun bindAuthRepository(authRepositoryBase: AuthRepository.Base): AuthRepository
}