package com.saer.telegramnew.auth.ui

import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.*
import com.saer.telegramnew.auth.communication.EnterPhoneUiCommunication
import com.saer.telegramnew.common.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
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
    private lateinit var testEnterPhoneUiCommunication: EnterPhoneUiCommunication
    private lateinit var testAuthRepository: TestAuthRepository
    private lateinit var testResources: Resources
    private var data: EnterPhoneUi? = null

    @Before
    fun setup() {
        testEnterPhoneUiCommunication = mock()
        Mockito.`when`(testEnterPhoneUiCommunication.map(any()))
            .thenAnswer {
                data = it.arguments[0] as EnterPhoneUi
                return@thenAnswer null
            }
        Mockito.`when`(testEnterPhoneUiCommunication.value).thenAnswer {
            return@thenAnswer data
        }

        testAuthRepository = TestAuthRepository()

        testResources = mock()
        Mockito.`when`(testResources.getInt(R.integer.phone_size)).thenReturn(11)
    }

    @After
    fun tearDown() {
        data = null
    }

    @Test
    fun `should to return SendCodeUi if phone number is correct and WaitEnterPhoneUi if phone number is incorrect `() =
        runTest {
            val viewModel = EnterPhoneNumberViewModel(
                testEnterPhoneUiCommunication,
                testAuthRepository,
                testResources,
                coroutineRule.testDispatcher
            )

            viewModel.enterPhoneNumber(phoneNumber)
            viewModel.sendCode()
            assertThat(testEnterPhoneUiCommunication.value)
                .isInstanceOf(expected::class.java)
        }

    companion object {
        @Parameterized.Parameters(name = "{index}: phone number = {0}, result = {1}")
        @JvmStatic
        fun data(): Collection<Array<Any>> {
            return listOf(
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
}