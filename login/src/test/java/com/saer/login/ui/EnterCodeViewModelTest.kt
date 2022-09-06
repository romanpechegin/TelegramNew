@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.saer.login.ui

import com.google.common.truth.Truth.assertThat
import com.saer.core.Communication
import com.saer.login.*
import com.saer.login.repositories.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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
    private val enterCodeUiCommunication: Communication<EnterCodeUi> = mock()
    private val resources: com.saer.core.Resources = mock()
    private val authRepository: AuthRepository = mock()

    private var enterCodeUiStateFlow: MutableStateFlow<EnterCodeUi?> = MutableStateFlow(null)

    @Before
    fun setup() = runTest {
        Mockito.`when`(enterCodeUiCommunication.map(data = any()))
            .thenAnswer {
                enterCodeUiStateFlow.tryEmit(it.arguments[0] as EnterCodeUi)
                return@thenAnswer null
            }
        Mockito.`when`(enterCodeUiCommunication.value).thenAnswer { enterCodeUiStateFlow.value }

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

        val enterCodeUis = mutableListOf<EnterCodeUi?>()
        val job = launch(coroutineRule.testDispatcher) {
            enterCodeUiStateFlow.toList(enterCodeUis)
        }

        assertThat(enterCodeUis.lastOrNull()).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)
        Mockito.verify(enterCodeUiCommunication, times(1)).map(any())
        viewModel.enterCode(code = code)

        if (code.length == resources.getInt(R.integer.code_size)) {
            Mockito.verify(enterCodeUiCommunication, times(3)).map(any())
            assertThat(enterCodeUis[enterCodeUis.size - 2]).isInstanceOf(EnterCodeUi.CompleteEnterCodeUi::class.java)
            assertThat(enterCodeUis.lastOrNull()).isInstanceOf(expected::class.java)
        } else {
            Mockito.verify(enterCodeUiCommunication, times(1)).map(any())
            assertThat(enterCodeUiCommunication.value).isInstanceOf(expected::class.java)
        }

        job.cancel()
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