package com.saer.telegramnew.interactors

import org.junit.Assert.*
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.CORRECT_PHONE_NUMBER
import com.saer.telegramnew.INCORRECT_PHONE_NUMBER
import com.saer.telegramnew.model.ErrorResult
import com.saer.telegramnew.model.SuccessResult

class AuthRepositoryTest {
    private val authRepository = AuthRepository.Base()

    @Test
    fun `test checkPhoneNumber`() {
        assertThat(authRepository.checkPhoneNumber(CORRECT_PHONE_NUMBER))
            .isEqualTo(SuccessResult<Boolean>())
        assertThat(authRepository.checkPhoneNumber(INCORRECT_PHONE_NUMBER))
            .isEqualTo(ErrorResult<Boolean>())
    }
}