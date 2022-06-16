package com.saer.telegramnew.auth.interactors

import android.util.Log
import com.saer.telegramnew.TelegramCredentials
import com.saer.telegramnew.auth.ui.RegisterUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.telegram.core.TelegramFlow
import kotlinx.telegram.coroutines.*
import kotlinx.telegram.flows.authorizationStateFlow
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

const val TOO_MANY_REQUESTS_EXCEPTION = "Too Many Requests: retry after "
const val PHONE_NUMBER_INVALID_EXCEPTION = "PHONE_NUMBER_INVALID"
const val PHONE_CODE_INVALID_EXCEPTION = "PHONE_CODE_INVALID"
const val UNAUTHORIZED_EXCEPTION = "Unauthorized"

interface AuthRepository {

    fun observeAuthState(): Flow<TdApi.AuthorizationState>
    suspend fun checkPhoneNumber(phoneNumber: String)
    suspend fun checkCode(code: String)
    suspend fun sendName(firstName: String, lastName: String)

    class Base @Inject constructor(
        private val api: TelegramFlow
    ) : AuthRepository {

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

        override suspend fun checkPhoneNumber(phoneNumber: String) {
            api.setAuthenticationPhoneNumber(phoneNumber, null)
        }

        override suspend fun checkCode(code: String) {
            api.checkAuthenticationCode(code)
        }

        override suspend fun sendName(firstName: String, lastName: String) {
            api.setName(firstName, lastName)
        }
    }
}
