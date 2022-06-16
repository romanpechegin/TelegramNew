package com.saer.telegramnew

import com.saer.telegramnew.auth.interactors.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.drinkless.td.libcore.telegram.TdApi

const val CORRECT_CODE_FOR_PASSWORD = "12345"
const val CORRECT_CODE = "00000"
const val INCORRECT_CODE = "11111"

const val UNIQUE_FIRST_NAME = "Roman"
const val BUSY_FIRST_NAME = "Ivan"

class TestAuthRepository : AuthRepository {
    private val authStateFlow =
        MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitTdlibParameters())

    override fun observeAuthState(): Flow<TdApi.AuthorizationState> = authStateFlow

    override suspend fun checkPhoneNumber(phoneNumber: String) {
        when (phoneNumber) {
            CORRECT_PHONE_NUMBER -> authStateFlow.emit(TdApi.AuthorizationStateWaitCode())
            else -> authStateFlow.emit(TdApi.AuthorizationStateWaitPhoneNumber())
        }
    }

    override suspend fun checkCode(code: String) {
        when (code) {
            CORRECT_CODE_FOR_PASSWORD -> authStateFlow.emit(TdApi.AuthorizationStateWaitPassword())
            CORRECT_CODE -> authStateFlow.emit(TdApi.AuthorizationStateReady())
            INCORRECT_CODE -> throw IllegalStateException()
            else -> authStateFlow.emit(TdApi.AuthorizationStateWaitCode())
        }
    }

    override suspend fun sendName(firstName: String, lastName: String) {
        when (firstName) {
            UNIQUE_FIRST_NAME -> authStateFlow.emit(TdApi.AuthorizationStateReady())
//            BUSY_FIRST_NAME -> authStateFlow.emit()
            else -> authStateFlow.emit(TdApi.AuthorizationStateWaitRegistration())
        }
    }
}