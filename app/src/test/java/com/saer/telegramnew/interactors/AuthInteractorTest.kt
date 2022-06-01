package com.saer.telegramnew.interactors

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.CORRECT_PHONE_NUMBER
import com.saer.telegramnew.INCORRECT_PHONE_NUMBER
import com.saer.telegramnew.model.ErrorResult
import com.saer.telegramnew.model.PendingResult
import com.saer.telegramnew.model.Result
import com.saer.telegramnew.model.SuccessResult
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.TdApi

private const val LOGIN = "login"
private const val PASS = "pass"
private const val SUCCESS_TOKEN = "success token"

class AuthInteractorTest {

    private val authInteractor = AuthInteractor.Base(TestAuthRepository())

    @Test
    fun `test checkPhoneNumber`() {
//        assertThat(authInteractor.checkPhoneNumber(CORRECT_PHONE_NUMBER))
//            .isEqualTo(SuccessResult<Boolean>())
//        assertThat(authInteractor.checkPhoneNumber(INCORRECT_PHONE_NUMBER))
//            .isEqualTo(ErrorResult<Boolean>())
    }

    class TestAuthRepository : AuthRepository {

        override suspend fun checkPhoneNumber(phoneNumber: String) {
//            return when (phoneNumber) {
//                CORRECT_PHONE_NUMBER -> SuccessResult()
//                INCORRECT_PHONE_NUMBER -> ErrorResult()
//                else -> PendingResult()
//            }
        }

        override fun observeAuthState(): Flow<TdApi.AuthorizationState> {
            TODO("Not yet implemented")
        }
    }
}