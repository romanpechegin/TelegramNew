package com.saer.login.repositories

import com.saer.api.TelegramException
import com.saer.api.TelegramFlow
import com.saer.api.coroutines.*
import com.saer.api.flows.authorizationStateFlow
import com.saer.api.flows.connectionStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi.*
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
    fun connectionState(): Flow<ConnectionState>
    suspend fun countries(): Countries
    suspend fun currentCountry(): Text
    fun observeAuthRequired(): Flow<Boolean>

    class Base @Inject constructor(
        private val api: TelegramFlow,
    ) : AuthRepository {

        override fun observeAuthState(): Flow<AuthorizationState> =
            api.authorizationStateFlow()

        override suspend fun checkPhoneNumber(phoneNumber: String) =
            api.setAuthenticationPhoneNumber(phoneNumber, null)

        override suspend fun checkCode(code: String) =
            api.checkAuthenticationCode(code)

        override suspend fun sendName(firstName: String, lastName: String) =
            api.setName(firstName, lastName)

        override suspend fun checkPassword(password: String) =
            api.checkAuthenticationPassword(password)

        override fun connectionState(): Flow<ConnectionState> = api.connectionStateFlow()

        override suspend fun countries(): Countries = api.getCountries()

        override suspend fun currentCountry(): Text = api.getCountryCode()

        override fun observeAuthRequired(): Flow<Boolean> =
            observeAuthState()
                .filter { it is AuthorizationStateWaitEncryptionKey }
                .map {
                    try {
                        api.getMe()
                    } catch (e: TelegramException) {
                        if (e.message == "Unauthorized") return@map true
                    }
                    false
                }
    }
}