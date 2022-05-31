package com.saer.telegramnew.interactors

import com.saer.telegramnew.model.Result

interface AuthInteractor {

    fun getToken(login: String, password: String): String
    fun checkPhoneNumber(phoneNumber: String): Result<Boolean>

    class Base(private val authRepository: AuthRepository) : AuthInteractor {
        override fun getToken(login: String, password: String): String {
            return authRepository.getToken(login, password)
        }

        override fun checkPhoneNumber(phoneNumber: String): Result<Boolean> {
            return authRepository.checkPhoneNumber(phoneNumber)
        }
    }
}