package com.saer.telegramnew.interactors

interface AuthRepository {

    fun getToken(login: String, password: String): String

    class Base: AuthRepository {
        override fun getToken(login: String, password: String): String {
            return "ewfsa234"
        }
    }
}
