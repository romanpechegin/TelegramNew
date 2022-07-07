@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.saer.login.ui

import com.google.common.truth.Truth.assertThat
import com.saer.core.Communication
import com.saer.login.CORRECT_PHONE_NUMBER
import com.saer.login.INCORRECT_PHONE_NUMBER
import com.saer.login.MainDispatcherRule
import com.saer.login.R
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class EnterPhoneNumberViewModelTest(
    private val phoneNumber: String,
    private val expected: EnterPhoneUi
) {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val testEnterPhoneUiCommunication: Communication<EnterPhoneUi> = mock()
    private val testAuthRepository: AuthRepository = mock()
    private val testResources: com.saer.core.Resources = mock()

    @Before
    fun setup() {
        var data: EnterPhoneUi? = null
        Mockito.`when`(testEnterPhoneUiCommunication.map(data = any()))
            .thenAnswer {
                data = it.arguments[0] as EnterPhoneUi
                return@thenAnswer null
            }
        Mockito.`when`(testEnterPhoneUiCommunication.value).thenAnswer { data }

        val authStateFlow =
            MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitTdlibParameters())
        Mockito.`when`(testAuthRepository.observeAuthState()).thenReturn(authStateFlow)
        runTest {
            Mockito.`when`(testAuthRepository.checkPhoneNumber(any()))
                .thenAnswer {
                    when (it.arguments[0]) {
                        CORRECT_PHONE_NUMBER -> authStateFlow.tryEmit(value = TdApi.AuthorizationStateWaitCode())
                        else -> authStateFlow.tryEmit(value = TdApi.AuthorizationStateWaitPhoneNumber())
                    }
                }
        }

        Mockito.`when`(testResources.getInt(resId = R.integer.phone_size)).thenReturn(11)
    }

    @Test
    fun `should to return SendCodeUi if phone number is correct and WaitEnterPhoneUi if phone number is incorrect `() =
        runTest {
            val viewModel = EnterPhoneNumberViewModel(
                enterPhoneUiCommunication = testEnterPhoneUiCommunication,
                authRepository = testAuthRepository,
                resources = testResources,
                ioDispatcher = coroutineRule.testDispatcher
            )

            viewModel.enterPhoneNumber(phoneNumber = phoneNumber)
            viewModel.sendCode()
            assertThat(testEnterPhoneUiCommunication.value)
                .isInstanceOf(expected::class.java)
        }

    companion object {
        @Parameterized.Parameters(name = "{index}: phone number = {0}, result = {1}")
        @JvmStatic
        fun data() = listOf(
            arrayOf("7989", EnterPhoneUi.WaitEnterPhoneUi()),
            arrayOf("1999", EnterPhoneUi.WaitEnterPhoneUi()),
            arrayOf(CORRECT_PHONE_NUMBER, EnterPhoneUi.SendCodeUi()),
            arrayOf("+7 (989) 263-47-7", EnterPhoneUi.WaitEnterPhoneUi()),
            arrayOf(INCORRECT_PHONE_NUMBER, EnterPhoneUi.WaitEnterPhoneUi()),
            arrayOf("7989263477asdf^00", EnterPhoneUi.WaitEnterPhoneUi()),
            arrayOf("+79892634770", EnterPhoneUi.SendCodeUi()),
            arrayOf("79892634770", EnterPhoneUi.SendCodeUi()),
            arrayOf("7989263477asdf^0", EnterPhoneUi.SendCodeUi())
        )
    }
}