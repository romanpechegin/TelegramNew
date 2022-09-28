@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.saer.login.ui

import com.google.common.truth.Truth.assertThat
import com.saer.core.Resources
import com.saer.core.common.InputMask
import com.saer.core.communications.Communication
import com.saer.login.CORRECT_PHONE_NUMBER
import com.saer.login.MainDispatcherRule
import com.saer.login.R
import com.saer.login.mappers.MapperAuthorisationStateToEnterPhoneUi
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
class EnterPhoneNumberViewModelTest(
    private val phoneNumber: String,
    private val expectedUi: EnterPhoneUi,
) {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val testEnterPhoneUiCommunication: Communication<EnterPhoneUi> = mock()
    private val inputMaskCommunication: Communication<InputMask> = mock()
    private val testAuthRepository: AuthRepository = mock()
    private val testResources: Resources = mock()
    private val mapper = MapperAuthorisationStateToEnterPhoneUi.Base()
    private lateinit var viewModel: EnterPhoneNumberViewModel
    private lateinit var enterPhoneUiList: MutableList<EnterPhoneUi>
    private lateinit var inputMasks: MutableList<InputMask>

    @Before
    fun setup() {
        runTest {
            enterPhoneUiList = mutableListOf()
            Mockito.`when`(testEnterPhoneUiCommunication.map(data = any()))
                .thenAnswer {
                    enterPhoneUiList.add(it.arguments[0] as EnterPhoneUi)
                    return@thenAnswer null
                }
            Mockito.`when`(testEnterPhoneUiCommunication.value)
                .thenAnswer { enterPhoneUiList.last() }

            inputMasks = mutableListOf()
            Mockito.`when`(inputMaskCommunication.map(any()))
                .thenAnswer {
                    inputMasks.add(it.arguments[0] as InputMask)
                    return@thenAnswer null
                }
            Mockito.`when`(inputMaskCommunication.value)
                .thenAnswer { inputMasks.last() }

            val authStateFlow =
                MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitTdlibParameters())
            Mockito.`when`(testAuthRepository.observeAuthState()).thenReturn(authStateFlow)
            Mockito.`when`(testAuthRepository.checkPhoneNumber(any()))
                .thenAnswer {
                    when (it.arguments[0]) {
                        CORRECT_PHONE_NUMBER -> authStateFlow.tryEmit(value = TdApi.AuthorizationStateWaitCode())
                        else -> authStateFlow.tryEmit(value = TdApi.AuthorizationStateWaitPhoneNumber())
                    }
                }

            Mockito.`when`(testAuthRepository.currentCountry()).thenReturn(TdApi.Text("RU"))
            Mockito.`when`(testAuthRepository.countries())
                .thenReturn(
                    TdApi.Countries(arrayOf(
                        TdApi.CountryInfo("RU", "Russia", "Russia", false, arrayOf("7")),
                        TdApi.CountryInfo("USA", "USA", "USA", false, arrayOf("1")),
                    ))
                )
        }

        Mockito.`when`(testResources.getInt(resId = R.integer.phone_size)).thenReturn(11)

        viewModel = EnterPhoneNumberViewModel(
            enterPhoneUiCommunication = testEnterPhoneUiCommunication,
            inputMaskCommunication = inputMaskCommunication,
            authRepository = testAuthRepository,
            resources = testResources,
            ioDispatcher = coroutineRule.testDispatcher,
            uiMapper = mapper
        )
    }

    @Test
    fun `should to return NavigateToEnterCodeUi if phone number is correct and WaitEnterPhoneUi if phone number is incorrect `() =
        runTest {
            Mockito.verify(testEnterPhoneUiCommunication, times(1)).map(any())
            assertThat(testEnterPhoneUiCommunication.value)
                .isInstanceOf(EnterPhoneUi.WaitEnterPhoneUi::class.java)

            viewModel.enterPhoneNumber(phoneNumber = phoneNumber)
            Mockito.verify(testEnterPhoneUiCommunication, times(1)).map(any())

            viewModel.checkPhoneNumber()
            if (phoneNumber.length == testResources.getInt(R.integer.phone_size)) {
                assertThat(enterPhoneUiList[enterPhoneUiList.size - 2])
                    .isInstanceOf(EnterPhoneUi.PendingResultSendingPhoneUi::class.java)
                assertThat(testEnterPhoneUiCommunication.value)
                    .isInstanceOf(expectedUi::class.java)
                Mockito.verify(testEnterPhoneUiCommunication, times(3)).map(any())
                viewModel.onStop()
                assertThat(testEnterPhoneUiCommunication.value).isInstanceOf(EnterPhoneUi.WaitEnterPhoneUi::class.java)
                Mockito.verify(testEnterPhoneUiCommunication, times(4)).map(any())
            } else {
                assertThat(testEnterPhoneUiCommunication.value)
                    .isInstanceOf(expectedUi::class.java)
                Mockito.verify(testEnterPhoneUiCommunication, times(2)).map(any())

                viewModel.enterPhoneNumber(phoneNumber = "${phoneNumber}1")
                assertThat(testEnterPhoneUiCommunication.value)
                    .isInstanceOf(EnterPhoneUi.WaitEnterPhoneUi::class.java)
                Mockito.verify(testEnterPhoneUiCommunication, times(3)).map(any())
            }
        }

    @Test
    fun `getting current country code`() = runTest {
        Mockito.verify(inputMaskCommunication, times(1)).map(any())
        assertThat(inputMaskCommunication.value).isInstanceOf(InputMask.PhoneNumberMask::class.java)
        assertThat(inputMaskCommunication.value.currentMaskName()).isEqualTo("Russia")
    }

    companion object {
        @Parameterized.Parameters(name = "{index}: phone number = {0}, result = {1}")
        @JvmStatic
        fun data() = listOf(
            arrayOf("7989", EnterPhoneUi.PhoneIsNotComplete()),
            arrayOf("1999", EnterPhoneUi.PhoneIsNotComplete()),
            arrayOf(CORRECT_PHONE_NUMBER, EnterPhoneUi.NavigateToEnterCodeUi()),
            arrayOf("+7 (989) 263-47-7", EnterPhoneUi.PhoneIsNotComplete()),
            arrayOf("7989263477asdf^00", EnterPhoneUi.PhoneIsNotComplete()),
            arrayOf("79892634770", EnterPhoneUi.NavigateToEnterCodeUi()),
        )
    }
}