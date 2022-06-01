package com.saer.telegramnew.interactors

import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi

interface AuthInteractor {

    fun observeAuthState(): Flow<TdApi.AuthorizationState>
    suspend fun checkPhoneNumber(phoneNumber: String)

    class Base(private val authRepository: AuthRepository) : AuthInteractor {
        override fun observeAuthState(): Flow<TdApi.AuthorizationState> =
            authRepository.observeAuthState()

        override suspend fun checkPhoneNumber(phoneNumber: String) {
            return authRepository.checkPhoneNumber(phoneNumber)
        }
    }
}