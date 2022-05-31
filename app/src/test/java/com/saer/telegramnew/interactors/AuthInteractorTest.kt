package com.saer.telegramnew.interactors

import org.junit.Test
import com.google.common.truth.Truth.assertThat

private const val LOGIN = "login"
private const val PASS = "pass"
private const val SUCCESS_TOKEN = "success token"

class AuthInteractorTest {

    @Test
    fun `test getToken`() {
        val authInteractor = AuthInteractor.Base(TestAuthRepository())
        assertThat(authInteractor.getToken(LOGIN, PASS)).isEqualTo(SUCCESS_TOKEN)
    }

    class TestAuthRepository : AuthRepository {
        override fun getToken(login: String, password: String): String =
            if (LOGIN == login && PASS == password) SUCCESS_TOKEN
            else "error"
    }
}