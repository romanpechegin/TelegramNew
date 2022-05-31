package com.saer.telegramnew.interactors

import com.saer.telegramnew.model.*

interface AuthRepository {

    fun getToken(login: String, password: String): String
    fun checkPhoneNumber(phoneNumber: String): Result<Boolean>

    class Base: AuthRepository {
        override fun getToken(login: String, password: String): String {
            return "ewfsa234"
        }

        override fun checkPhoneNumber(phoneNumber: String): Result<Boolean> {
            return SuccessResult()
        }
    }
}
