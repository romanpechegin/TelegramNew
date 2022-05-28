package com.saer.telegramnew.interactors

interface AuthInteractor {

    fun getToken(login: String, password: String): String

    class Base(private val authRepository: AuthRepository) : AuthInteractor {
        override fun getToken(login: String, password: String): String {
            return authRepository.getToken(login, password)
        }
    }
}