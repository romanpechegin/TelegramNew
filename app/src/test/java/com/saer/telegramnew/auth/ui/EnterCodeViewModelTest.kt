@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.saer.telegramnew.auth.ui

import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.*
import com.saer.telegramnew.auth.communication.EnterCodeUiCommunication
import com.saer.telegramnew.auth.interactors.AuthRepository
import com.saer.telegramnew.common.Resources
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
import org.mockito.kotlin.times

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class EnterCodeViewModelTest(
    private val code: String,
    private val expected: EnterCodeUi
) {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val enterCodeUiCommunication: EnterCodeUiCommunication = mock()
    private val resources: Resources = mock()
    private val authRepository: AuthRepository = mock()

    @Before
    fun setup() = runTest {
        var data: EnterCodeUi? = null
        Mockito.`when`(enterCodeUiCommunication.map(data = any()))
            .thenAnswer {
                data = it.arguments[0] as EnterCodeUi
                return@thenAnswer null
            }
        Mockito.`when`(enterCodeUiCommunication.value).thenAnswer { data }

        val authStateFlow =
            MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitTdlibParameters())
        Mockito.`when`(authRepository.observeAuthState()).thenReturn(authStateFlow)
        Mockito.`when`(authRepository.checkCode(code = any()))
            .thenAnswer {
                when (it.arguments[0]) {
                    CORRECT_CODE_FOR_PASSWORD -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitPassword())
                    CORRECT_CODE -> authStateFlow.tryEmit(TdApi.AuthorizationStateReady())
                    INCORRECT_CODE -> throw IllegalStateException()
                    else -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitCode())
                }
            }

        Mockito.`when`(resources.getInt(R.integer.code_size)).thenReturn(5)
    }

    @Test
    fun `test enter code `() = runTest {
        val viewModel =
            EnterCodeViewModel(
                ioDispatcher = coroutineRule.testDispatcher,
                enterCodeUiCommunication = enterCodeUiCommunication,
                authRepository = authRepository,
                resources = resources
            )

        Mockito.verify(enterCodeUiCommunication, times(1)).map(any())
        viewModel.enterCode(code = code)
        assertThat(enterCodeUiCommunication.value).isInstanceOf(expected::class.java)
    }

    companion object {
        @Parameterized.Parameters(name = "{index}: code = {0}, result = {1}")
        @JvmStatic
        fun data() = listOf(
            arrayOf("", EnterCodeUi.WaitCodeUi()),
            arrayOf("123", EnterCodeUi.WaitCodeUi()),
            arrayOf(CORRECT_CODE_FOR_PASSWORD, EnterCodeUi.WaitPasswordUi()),
            arrayOf(INCORRECT_CODE, EnterCodeUi.ErrorCodeUi(IllegalStateException())),
            arrayOf(CORRECT_CODE, EnterCodeUi.SuccessAuthUi()),
            arrayOf("asdffdsafads", EnterCodeUi.WaitCodeUi()),
        )
    }
}