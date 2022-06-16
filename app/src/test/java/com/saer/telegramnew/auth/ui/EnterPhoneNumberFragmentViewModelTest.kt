package com.saer.telegramnew.auth.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.MainDispatcherRule
import com.saer.telegramnew.R
import com.saer.telegramnew.TestAuthRepository
import com.saer.telegramnew.common.Resources
import com.saer.telegramnew.auth.communication.EnterPhoneUiCommunication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    private val testAuthRepository = TestAuthRepository()
    private val testResources = mock<Resources>()
    private val viewModel by lazy {
        EnterPhoneNumberFragmentViewModel(
            testEnterPhoneUiCommunication,
            testAuthRepository,
            testResources,
            coroutineRule.testDispatcher
        )
    }

    @Before
    fun setup() {
        Mockito.`when`(testResources.getInt(R.integer.phone_size)).thenReturn(11)
    }

    @Test
    fun `test enter phone number`() = runTest {
        assertThat(testEnterPhoneUiCommunication.result)
            .isEqualTo(null)

        viewModel.enterPhoneNumber("7")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)

        viewModel.enterPhoneNumber("")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)

        viewModel.enterPhoneNumber("+79892634770")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.CompleteInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.SendCodeUi::class.java)

        viewModel.enterPhoneNumber("+7 (989) 263-47-7")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)

        viewModel.enterPhoneNumber("79892634770")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.CompleteInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.SendCodeUi::class.java)

        viewModel.enterPhoneNumber("7989263477")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.WaitInputPhoneUi::class.java)

        viewModel.enterPhoneNumber("7989263477asdf^0")
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.CompleteInputPhoneUi::class.java)
        viewModel.sendCode()
        assertThat(testEnterPhoneUiCommunication.result).isInstanceOf(EnterPhoneUi.SendCodeUi::class.java)

        viewModel.enterPhoneNumber("7989263477asdf^00")
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
}