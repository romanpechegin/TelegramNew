package com.saer.login.repositories

import android.util.Log
import com.saer.api.TelegramCredentials
import com.saer.core.di.LoginFeature
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.telegram.core.TelegramFlow
import kotlinx.telegram.coroutines.*
import kotlinx.telegram.flows.authorizationStateFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState
import javax.inject.Inject

const val TOO_MANY_REQUESTS_EXCEPTION = "Too Many Requests: retry after "
const val PHONE_NUMBER_INVALID_EXCEPTION = "PHONE_NUMBER_INVALID"
const val PHONE_CODE_INVALID_EXCEPTION = "PHONE_CODE_INVALID"
const val UNAUTHORIZED_EXCEPTION = "Unauthorized"

interface AuthRepository {

    fun observeAuthState(): Flow<AuthorizationState>
    suspend fun checkPhoneNumber(phoneNumber: String)
    suspend fun checkCode(code: String)
    suspend fun sendName(firstName: String, lastName: String)
    suspend fun checkPassword(password: String)

    @LoginFeature
    class Base @Inject constructor(
        private val api: TelegramFlow
    ) : AuthRepository {

        override fun observeAuthState(): Flow<AuthorizationState> =
            api.authorizationStateFlow()
                .onEach {
                    when (it) {
                        is TdApi.AuthorizationStateWaitTdlibParameters ->
                            api.setTdlibParameters(TelegramCredentials.parameters)
                        is TdApi.AuthorizationStateWaitEncryptionKey ->
                            api.checkDatabaseEncryptionKey(null)
                    }
                }

        override suspend fun checkPhoneNumber(phoneNumber: String) =
            api.setAuthenticationPhoneNumber(phoneNumber, null)

        override suspend fun checkCode(code: String) =
            api.checkAuthenticationCode(code)

        override suspend fun sendName(firstName: String, lastName: String) =
            api.setName(firstName, lastName)

        override suspend fun checkPassword(password: String) =
            api.checkAuthenticationPassword(password)

        init {
            GlobalScope.launch {
                test()
            }
        }
        suspend fun test() {
            api.getCountryCode()
            Log.e("TAG", "test: ${api.getCountryCode()}")
        }
    }
}
