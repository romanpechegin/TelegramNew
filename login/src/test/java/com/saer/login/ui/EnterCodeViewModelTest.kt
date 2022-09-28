@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.saer.login.ui

import com.google.common.truth.Truth.assertThat
import com.saer.core.communications.Communication
import com.saer.login.*
import com.saer.login.mappers.MapperAuthorisationStateToEnterCodeUi
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
import org.mockito.kotlin.times

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class EnterCodeViewModelTest(
    private val code: String,
    private val expectedUi: EnterCodeUi,
) {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val enterCodeUiCommunication: Communication<EnterCodeUi> = mock()
    private val resources: com.saer.core.Resources = mock()
    private val authRepository: AuthRepository = mock()
    private lateinit var viewModel: EnterCodeViewModel
    private val mapperAuthorisationStateToEnterCodeUi = MapperAuthorisationStateToEnterCodeUi.Base()

    private lateinit var enterCodeUiList: MutableList<EnterCodeUi>

    @Before
    fun setup() = runTest {
        enterCodeUiList = mutableListOf()
        Mockito.`when`(enterCodeUiCommunication.map(data = any()))
            .thenAnswer {
                enterCodeUiList.add(it.arguments[0] as EnterCodeUi)
                return@thenAnswer null
            }
        Mockito.`when`(enterCodeUiCommunication.value).thenAnswer { enterCodeUiList.last() }

        val authStateFlow =
            MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitTdlibParameters())
        Mockito.`when`(authRepository.observeAuthState()).thenReturn(authStateFlow)
        Mockito.`when`(authRepository.checkCode(code = any()))
            .thenAnswer {
                when (it.arguments[0]) {
                    CORRECT_CODE_FOR_PASSWORD -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitPassword())
                    CORRECT_CODE -> authStateFlow.tryEmit(TdApi.AuthorizationStateReady())
                    INCORRECT_CODE -> throw IllegalStateException()
                    UNREGISTER_PHONE_NUMBER -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitRegistration())
                    else -> authStateFlow.tryEmit(TdApi.AuthorizationStateWaitCode())
                }
            }

        Mockito.`when`(resources.getInt(R.integer.code_size)).thenReturn(5)

        viewModel =
            EnterCodeViewModel(
                ioDispatcher = coroutineRule.testDispatcher,
                enterCodeUiCommunication = enterCodeUiCommunication,
                authRepository = authRepository,
                resources = resources,
                mapperAuthorisationStateToEnterCodeUi = mapperAuthorisationStateToEnterCodeUi
            )
    }

    @Test
    fun `test enter code `() = runTest {
        Mockito.verify(enterCodeUiCommunication, times(1)).map(any())
        assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)

        viewModel.enterCode(code = code)

        if (code.length == resources.getInt(R.integer.code_size)) {
            assertThat(enterCodeUiList[enterCodeUiList.size - 2]).isInstanceOf(EnterCodeUi.CompleteEnterCodeUi::class.java)
            assertThat(enterCodeUiCommunication.value).isInstanceOf(expectedUi::class.java)
            Mockito.verify(enterCodeUiCommunication, times(3)).map(any())

            viewModel.onStop()
            if (enterCodeUiCommunication.value is EnterCodeUi.NavigateToEnterPasswordUi ||
                enterCodeUiCommunication.value is EnterCodeUi.NavigateToRegistrationUi
            ) {
                assertThat(enterCodeUiCommunication.value).isInstanceOf(EnterCodeUi.WaitCodeUi::class.java)
                Mockito.verify(enterCodeUiCommunication, times(4)).map(any())
            } else if (enterCodeUiCommunication.value is EnterCodeUi.SuccessAuthUi) {
                assertThat(enterCodeUiCommunication.value).isInstanceOf(expectedUi::class.java)
                Mockito.verify(enterCodeUiCommunication, times(3)).map(any())
            }
        } else {
            assertThat(enterCodeUiCommunication.value).isInstanceOf(expectedUi::class.java)
            Mockito.verify(enterCodeUiCommunication, times(1)).map(any())
        }
    }

    companion object {
        @Parameterized.Parameters(name = "{index}: code = {0}, result = {1}")
        @JvmStatic
        fun data() = listOf(
            arrayOf("", EnterCodeUi.WaitCodeUi()),
            arrayOf("123", EnterCodeUi.WaitCodeUi()),
            arrayOf(CORRECT_CODE_FOR_PASSWORD, EnterCodeUi.NavigateToEnterPasswordUi()),
            arrayOf(INCORRECT_CODE, EnterCodeUi.ErrorCodeUi(IllegalStateException())),
            arrayOf(CORRECT_CODE, EnterCodeUi.SuccessAuthUi()),
            arrayOf(UNREGISTER_PHONE_NUMBER, EnterCodeUi.NavigateToRegistrationUi()),
            arrayOf("asdffdsafads", EnterCodeUi.WaitCodeUi()),
        )
    }
}