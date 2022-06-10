package com.saer.telegramnew.interactors

import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi

interface AuthInteractor {

    fun observeAuthState(): Flow<TdApi.AuthorizationState>
    suspend fun checkPhoneNumber(phoneNumber: String)
    suspend fun checkCode(code: String)

    class Base(private val authRepository: AuthRepository) : AuthInteractor {
        override fun observeAuthState(): Flow<TdApi.AuthorizationState> =
            authRepository.observeAuthState()

        override suspend fun checkPhoneNumber(phoneNumber: String) {
            authRepository.checkPhoneNumber(phoneNumber)
        }

        override suspend fun checkCode(code: String) {
            authRepository.checkCode(code)
        }
    }
}