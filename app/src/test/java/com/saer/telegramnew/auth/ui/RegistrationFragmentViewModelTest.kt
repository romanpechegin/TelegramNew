package com.saer.telegramnew.auth.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.saer.telegramnew.MainDispatcherRule
import org.junit.Assert.*
import com.google.common.truth.Truth.assertThat
import com.saer.telegramnew.BUSY_FIRST_NAME
import com.saer.telegramnew.TestAuthRepository
import com.saer.telegramnew.UNIQUE_FIRST_NAME
import com.saer.telegramnew.auth.communication.RegistrationUiCommunication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationFragmentViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val registrationUiCommunication = TestRegistrationUiCommunication()
    private val authRepository = TestAuthRepository()

    private val viewModel by lazy {
        RegistrationFragmentViewModel(
            authRepository,
            registrationUiCommunication,
            coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test enter first and last name`() = runTest {

        assertThat(registrationUiCommunication.value).isEqualTo(null)

        viewModel.firstName = ""
        viewModel.lastName = ""
        assertThat(registrationUiCommunication.value).isInstanceOf(RegisterUi.WaitEnterNameUi::class.java)

        viewModel.firstName = ""
        viewModel.lastName = ""
        viewModel.registerUser()
        assertThat(registrationUiCommunication.value).isInstanceOf(RegisterUi.EnterFirstNameUi::class.java)

        viewModel.firstName = UNIQUE_FIRST_NAME
        viewModel.lastName = ""
        viewModel.registerUser()
        assertThat(registrationUiCommunication.value).isInstanceOf(RegisterUi.SuccessRegisterUi::class.java)

        viewModel.firstName = ""
        viewModel.lastName = "Ifasdfa"
        viewModel.registerUser()
        assertThat(registrationUiCommunication.value).isInstanceOf(RegisterUi.EnterFirstNameUi::class.java)

    }

    class TestRegistrationUiCommunication : RegistrationUiCommunication {
        private var result: RegisterUi? = null
        override val value: RegisterUi?
            get() = result

        override fun map(data: RegisterUi) {
            result = data
        }

        override fun observe(viewLifecycleOwner: LifecycleOwner, observer: Observer<RegisterUi>) {}
    }
}