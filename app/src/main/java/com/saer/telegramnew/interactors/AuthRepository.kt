package com.saer.telegramnew.interactors

import com.saer.telegramnew.TelegramCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.telegram.core.TelegramFlow
import kotlinx.telegram.coroutines.checkAuthenticationCode
import kotlinx.telegram.coroutines.checkDatabaseEncryptionKey
import kotlinx.telegram.coroutines.setAuthenticationPhoneNumber
import kotlinx.telegram.coroutines.setTdlibParameters
import kotlinx.telegram.flows.authorizationStateFlow
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

const val TOO_MANY_REQUESTS_EXCEPTION = "Too Many Requests: retry after "
interface AuthRepository {

    fun observeAuthState(): Flow<TdApi.AuthorizationState>
    suspend fun checkPhoneNumber(phoneNumber: String)
    suspend fun checkCode(code: String)

    class Base @Inject constructor(
        private val api: TelegramFlow
    ) : AuthRepository {

        override fun observeAuthState(): Flow<TdApi.AuthorizationState> =
            api.authorizationStateFlow()
                .onEach {
                    when (it) {
                        is TdApi.AuthorizationStateWaitTdlibParameters ->
                            api.setTdlibParameters(TelegramCredentials.parameters)
                        is TdApi.AuthorizationStateWaitEncryptionKey ->
                            api.checkDatabaseEncryptionKey(null)
                    }
                }

        override suspend fun checkPhoneNumber(phoneNumber: String) {
            api.setAuthenticationPhoneNumber(phoneNumber, null)
        }

        override suspend fun checkCode(code: String) {
            api.checkAuthenticationCode(code)
        }
    }
}
