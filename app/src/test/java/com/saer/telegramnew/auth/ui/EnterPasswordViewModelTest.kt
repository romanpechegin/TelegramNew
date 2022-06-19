package com.saer.telegramnew.auth.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.CORRECT_PASSWORD
import com.saer.telegramnew.INCORRECT_PASSWORD
import com.saer.telegramnew.MainDispatcherRule
import com.saer.telegramnew.auth.communication.EnterPasswordUiCommunication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EnterPasswordViewModelTest {

    private val enterPasswordUiCommunication = TestEnterPasswordUiCommunication()

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Test
    fun `test enter password`() = runTest {
//        val viewModel: EnterPasswordViewModel = EnterPasswordViewModel()

//        assertThat(enterPasswordUiCommunication.value).isEqualTo(null)
//
//        viewModel.enterPassword("")
//        assertThat(enterPasswordUiCommunication.value).isInstanceOf(EnterPasswordUi.WainPasswordUi::class.java)
//
//        viewModel.enterPassword(INCORRECT_PASSWORD)
//        assertThat(enterPasswordUiCommunication.value).isInstanceOf(EnterPasswordUi.IncorrectPasswordUi::class.java)
//
//        viewModel.enterPassword(CORRECT_PASSWORD)
//        assertThat(enterPasswordUiCommunication.value).isInstanceOf(EnterPasswordUi.SuccessPasswordUi::class.java)

    }

    class TestEnterPasswordUiCommunication : EnterPasswordUiCommunication {
        private var result: EnterPasswordUi? = null

        override val value: EnterPasswordUi?
            get() = result

        override fun map(data: EnterPasswordUi) {
            result = data
        }

        override fun observe(
            viewLifecycleOwner: LifecycleOwner,
            observer: Observer<EnterPasswordUi>
        ) {}
    }
}