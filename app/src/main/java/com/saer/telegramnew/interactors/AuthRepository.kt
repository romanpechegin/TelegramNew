package com.saer.telegramnew.interactors

import android.util.Log
import com.saer.telegramnew.TelegramCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.telegram.core.TelegramFlow
import kotlinx.telegram.coroutines.checkDatabaseEncryptionKey
import kotlinx.telegram.coroutines.setAuthenticationPhoneNumber
import kotlinx.telegram.coroutines.setTdlibParameters
import kotlinx.telegram.flows.authorizationStateFlow
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

interface AuthRepository {

    suspend fun checkPhoneNumber(phoneNumber: String)
    fun observeAuthState(): Flow<TdApi.AuthorizationState>

    class Base @Inject constructor(
        private val api: TelegramFlow
    ) : AuthRepository {

        override suspend fun checkPhoneNumber(phoneNumber: String) {
            return api.setAuthenticationPhoneNumber(phoneNumber, null)
        }

        override fun observeAuthState(): Flow<TdApi.AuthorizationState> =
            api.authorizationStateFlow()
                .onEach {
                    Log.e("TAG", "observeAuthState: ${it.javaClass.simpleName}")
                    when (it) {
                        is TdApi.AuthorizationStateWaitTdlibParameters ->
                            api.setTdlibParameters(TelegramCredentials.parameters)
                        is TdApi.AuthorizationStateWaitEncryptionKey ->
                            api.checkDatabaseEncryptionKey(null)
                    }
                }
    }
}
