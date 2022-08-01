package com.saer.login.ui

import com.google.common.truth.Truth.assertThat
import com.saer.core.Communication
import com.saer.login.CORRECT_PASSWORD
import com.saer.login.INCORRECT_PASSWORD
import com.saer.login.MainDispatcherRule
import com.saer.login.UNREGISTER_PASSWORD
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.drinkless.td.libcore.telegram.TdApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class EnterPasswordViewModelTest(
    private val password: String,
    private val expectedUi: EnterPasswordUi
) {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val enterPasswordUiCommunication: Communication<EnterPasswordUi> = mock()
    private val authRepository: AuthRepository = mock()

    @Before
    fun setup() = runTest {
        var data: EnterPasswordUi? = null
        Mockito.`when`(enterPasswordUiCommunication.value).thenReturn(data)
        Mockito.`when`(enterPasswordUiCommunication.map(any()))
            .thenAnswer {
                data = it.arguments[0] as EnterPasswordUi
                return@thenAnswer null
            }

        val authStateFlow =
            MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitTdlibParameters())
        Mockito.`when`(authRepository.observeAuthState()).thenReturn(authStateFlow)
        Mockito.`when`(authRepository.checkPassword(any()))
            .thenAnswer {
                when (it.arguments[0]) {
                    CORRECT_PASSWORD -> authStateFlow.tryEmit(TdApi.AuthorizationStateReady())
                    INCORRECT_PASSWORD -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitPassword())
                    UNREGISTER_PASSWORD -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitRegistration())
                    else -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitPassword())
                }
            }
    }

    @Test
    fun `test enter password`() = runTest {
        val viewModel =
            EnterPasswordViewModel(
                authRepository,
                enterPasswordUiCommunication,
                coroutineRule.testDispatcher
            )

        viewModel.enterPassword(password)
        assertThat(enterPasswordUiCommunication.value).isInstanceOf(expectedUi::class.java)
    }

    companion object {
        @Parameterized.Parameters(name = "{index}, password = {0}, expectedUi = {1}")
        @JvmStatic
        fun data() = listOf(
            arrayOf("", EnterPasswordUi.Wait()),
            arrayOf(INCORRECT_PASSWORD, EnterPasswordUi.IncorrectPassword()),
            arrayOf(UNREGISTER_PASSWORD, EnterPasswordUi.Registration()),
            arrayOf(CORRECT_PASSWORD, EnterPasswordUi.Success()),
        )
    }
}