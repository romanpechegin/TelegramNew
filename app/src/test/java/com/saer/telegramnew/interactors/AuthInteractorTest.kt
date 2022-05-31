package com.saer.telegramnew.interactors

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.CORRECT_PHONE_NUMBER
import com.saer.telegramnew.INCORRECT_PHONE_NUMBER
import com.saer.telegramnew.model.ErrorResult
import com.saer.telegramnew.model.PendingResult
import com.saer.telegramnew.model.Result
import com.saer.telegramnew.model.SuccessResult

private const val LOGIN = "login"
private const val PASS = "pass"
private const val SUCCESS_TOKEN = "success token"

class AuthInteractorTest {

    private val authInteractor = AuthInteractor.Base(TestAuthRepository())

    @Test
    fun `test getToken`() {

        assertThat(authInteractor.getToken(LOGIN, PASS)).isEqualTo(SUCCESS_TOKEN)
    }

    @Test
    fun `test checkPhoneNumber`() {
        assertThat(authInteractor.checkPhoneNumber(CORRECT_PHONE_NUMBER))
            .isEqualTo(SuccessResult<Boolean>())
        assertThat(authInteractor.checkPhoneNumber(INCORRECT_PHONE_NUMBER))
            .isEqualTo(ErrorResult<Boolean>())
    }

    class TestAuthRepository : AuthRepository {
        override fun getToken(login: String, password: String): String =
            if (LOGIN == login && PASS == password) SUCCESS_TOKEN
            else "error"

        override fun checkPhoneNumber(phoneNumber: String): Result<Boolean> {
            return when (phoneNumber) {
                CORRECT_PHONE_NUMBER -> SuccessResult()
                INCORRECT_PHONE_NUMBER -> ErrorResult()
                else -> PendingResult()
            }
        }
    }
}