package com.saer.telegramnew.interactors

import org.junit.Assert.*
import org.junit.Test

private const val LOGIN = "login"
private const val PASS = "pass"
private const val SUCCESS_TOKEN = "success token"

class AuthInteractorTest {

    @Test
    fun `test getToken`() {
        val authInteractor = AuthInteractor.Base(TestAuthRepository())
        assertEquals(SUCCESS_TOKEN, authInteractor.getToken(LOGIN, PASS))
    }

    class TestAuthRepository : AuthRepository {
        override fun getToken(login: String, password: String): String =
            if (LOGIN == login && PASS == password) SUCCESS_TOKEN
            else "error"
    }
}