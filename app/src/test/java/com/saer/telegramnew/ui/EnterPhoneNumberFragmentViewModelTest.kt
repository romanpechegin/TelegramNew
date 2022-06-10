package com.saer.telegramnew.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.CORRECT_PHONE_NUMBER
import com.saer.telegramnew.MainDispatcherRule
import com.saer.telegramnew.R
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.communications.EnterPhoneUiCommunication
import com.saer.telegramnew.interactors.AuthInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.drinkless.td.libcore.telegram.TdApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class EnterPhoneNumberFragmentViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val testEnterPhoneUiCommunication = TestEnterPhoneUiCommunication()
    private val testAuthInteractor = TestAuthInteractor()
    private val testResources = mock<Resources>()
    private val viewModel by lazy {
        EnterPhoneNumberFragmentViewModel(
            testEnterPhoneUiCommunication,
            testAuthInteractor,
            testResources,
            coroutineRule.testDispatcher
        )
    }

    @Before
    fun setup() {
        Mockito.`when`(testResources.getInt(R.integer.phone_size)).thenReturn(11)
    }

    @Test
    fun `test input phone number`() = runTest {
        assertThat(testEnterPhoneUiCommunication.result)
            .isEqualTo(null)

        viewModel.inputPhoneNumber("7")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)

        viewModel.inputPhoneNumber("")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)

        viewModel.inputPhoneNumber("+79892634770")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.CompleteInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.SendCodeUi::class.java)

        viewModel.inputPhoneNumber("+7 (989) 263-47-7")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)

        viewModel.inputPhoneNumber("79892634770")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.CompleteInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.SendCodeUi::class.java)

        viewModel.inputPhoneNumber("7989263477")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)

        viewModel.inputPhoneNumber("7989263477asdf^0")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.CompleteInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.SendCodeUi::class.java)

        viewModel.inputPhoneNumber("7989263477asdf^00")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)
    }

    class TestEnterPhoneUiCommunication : EnterPhoneUiCommunication {
        var count = 0
        var result: EnterPhoneUi? = null

        override val value: EnterPhoneUi?
            get() = result

        override fun map(data: EnterPhoneUi) {
            count++
            result = data
        }

        override fun observe(
            viewLifecycleOwner: LifecycleOwner,
            observer: Observer<EnterPhoneUi>
        ) = Unit
    }

    class TestAuthInteractor : AuthInteractor {
        private var phoneNumber: String? = null
        private val authStateFlow = MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitPhoneNumber())

        override fun observeAuthState(): Flow<TdApi.AuthorizationState> = authStateFlow

        override suspend fun checkPhoneNumber(phoneNumber: String) {
            this.phoneNumber = phoneNumber
            when (phoneNumber) {
                CORRECT_PHONE_NUMBER -> authStateFlow.emit(TdApi.AuthorizationStateWaitCode())
                else -> authStateFlow.emit(TdApi.AuthorizationStateWaitPhoneNumber())
            }
        }

        override suspend fun checkCode(code: String) {}
    }
}